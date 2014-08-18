package org.zenworks.zookeeper.explorer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.zenworks.common.Common;
import org.zenworks.common.config.ConfigKey;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.zenworks.gui.DialogBox;
import org.zenworks.gui.DialogResult;
import org.zenworks.gui.GuiUtils;

// DONE Refresh does not work for the tree
// TODO ComboBox change does not make the tree change (not always)
// DONE That might be a problem with the refresh (if a node is added in the background it does not show up as changed).
// DONE Vezerlok letiltesa ha ZKhoz kapcsolodunk ujra (Refresh)
// TODO Dialógusablak a GUI mögé megy néha, nem lehet látni az ablakot a nagyobb ablaktól.
public class MainWindowController implements Initializable {

	@FXML
	private TreeView<ZooKeeperNode> zkTree;
	
	@FXML
	private TextArea content;

    @FXML
    private Button connectButton;
	
	@FXML
	private ComboBox<String> zkConnectString;
	
	ZooKeeperAdapter adapter = null;
	
	String pathInTree=INVALID;
	
	final static String INVALID="";
	
	@FXML
	private ImageView contentChange;

    @FXML
    Button newButton;

    @FXML
    Button cloneButton;

    @FXML
    Button deleteButton;

    @FXML
    Button onRefresh;

    @FXML
    Button importButton;

    @FXML
    Button exportButton;

    @FXML
    Button restoreButton;

    @FXML
    Button saveButton;

    @FXML
    Label statLabel;

    @FXML
    Button findButton;

    @FXML
    Button findNextButton;

    private String lastSearchQuery="";

	public void initialize(URL arg0, ResourceBundle arg1) {
		
		if (Common.getConfig().isStringArrayConfig(ConfigKey.ZOOKEEPER_FAVORITES)) {
			String[] zkFavorites = Common.getConfig().getStringArrayConfig(ConfigKey.ZOOKEEPER_FAVORITES); 
			for (String favConnect:zkFavorites) {
	 		   zkConnectString.getItems().add(favConnect);
	 		}
			
			if (zkFavorites.length>1) {
	 		   zkConnectString.getEditor().replaceSelection(zkFavorites[0]);
	 		   zkConnectString.getSelectionModel().selectFirst();
	 		   onConnect();
	 		}
		}

 		zkTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<ZooKeeperNode>>() {

			public void changed(ObservableValue<? extends TreeItem<ZooKeeperNode>> arg0, TreeItem<ZooKeeperNode> arg1,
					TreeItem<ZooKeeperNode> arg2) {
				if (arg2==null) {
					pathInTree = INVALID;
				} else {
					pathInTree=arg2.getValue().getPath();
				}
				refreshContent();
			}		
		});	
		
		zkTree.setCellFactory(new Callback<TreeView<ZooKeeperNode>, TreeCell<ZooKeeperNode>>() {

			public TreeCell<ZooKeeperNode> call(TreeView<ZooKeeperNode> arg0) {
			   return new ZooKeeperTreeItem(new Callback<String, Object>() {
				
				public Object call(String arg0) {
					String withoutLeaf = pathInTree.substring(0, pathInTree.lastIndexOf("/"));
					adapter.renameNode(pathInTree,withoutLeaf+arg0);
					System.out.println("Renaming "+pathInTree+" to " + withoutLeaf+"/"+arg0);
					return null;
				}
			});
			}
		});
		zkTree.setEditable(false);      
		content.setDisable(false);
        content.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
               if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.F && !content.isDisabled()) {
                   onFind(null);
               }
            }
        });
        statLabel.setText("Not connected.");
		
	}

	@FXML
	private void onNew() {
	   String rootWithSlash = (pathInTree==INVALID?"/":pathInTree+"/");
       String newNode = DialogBox.showQueryDialog("Enter the name of the node to create under `"+rootWithSlash+"`:");
       if (newNode != null) {
           adapter.createNode(rootWithSlash+newNode,"");
           refreshTree();
           DialogBox.showMessageDialog(rootWithSlash+newNode+" created successfully.");
       }
    }
	
	@FXML
	private void onClone() {
		if (pathInTree != INVALID) {
			String withoutLeaf = pathInTree.substring(0, pathInTree.lastIndexOf("/"));
			String leafName = pathInTree.substring(pathInTree.lastIndexOf("/")+1, pathInTree.length());
			
			int count=1;
			String newNodeName=leafName+count;
			while(adapter.isNode(withoutLeaf+"/"+newNodeName)) {
			   count=count+1;
			   newNodeName=leafName+count;
			}
			adapter.createNode(withoutLeaf + "/" + newNodeName, adapter.getNodeData(pathInTree));
			refreshTree();
		}				   
	}
	
	@FXML
	private void onImport() {
		 JFileChooser fileChooser = new JFileChooser();
		 fileChooser.showDialog(null, "Import");
		 try {
			 File importFile = fileChooser.getSelectedFile();
             byte[] fileContent = FileUtils.readFileToByteArray(importFile);
             String importNode = JOptionPane.showInputDialog(null, "What shall be the name of the new node under `" + (pathInTree == INVALID ? "/" : pathInTree) + "`?");
		     String targetPath = (pathInTree==INVALID?"/"+importNode:pathInTree+"/"+importNode);
			 adapter.createNode(targetPath,fileContent);
             DialogBox.showConfirmationDialog(importFile.getAbsolutePath() + " was imported to " + targetPath);
			 refreshTree();			 
		 } catch (IOException exc) {
             DialogBox.showConfirmationDialog("Could not import file.");
		 }	 
	   
	}
	
	@FXML
	private void onExport() {
		 JFileChooser fileChooser = new JFileChooser();
		 fileChooser.showDialog(null, "Export");
		 File exportFile = fileChooser.getSelectedFile();
		 if (pathInTree!=INVALID) {
			 try {
                 FileUtils.writeByteArrayToFile(exportFile, adapter.getNodeDataBytes(pathInTree));
                 DialogBox.showConfirmationDialog(pathInTree + " was exported to " + exportFile.getAbsolutePath());
			 } catch (IOException exc) {
                 DialogBox.showConfirmationDialog("Could not export file.");
			 }
		 }
		
	}
	
	@FXML
	private void onContentChange() {
		contentChange.setVisible(true);
		
	}
	
	@FXML
	private void onRestore() {
		if (pathInTree!=INVALID && contentChange.isVisible()) {
			if (DialogBox.showConfirmationDialog("Do you want to restore the original content of `"+pathInTree+"`?") == DialogResult.YES) {
				refreshContent();
				contentChange.setVisible(false);
			}
						
		}		
	}

	@FXML
	private void onSave() {		
	   if (pathInTree!=INVALID) {
		  if (contentChange.isVisible()) {
			  adapter.setNodeData(pathInTree, content.getText());
			  contentChange.setVisible(false);
              DialogBox.showMessageDialog("Saved " + content.getText().getBytes().length + " bytes to " + pathInTree);
		  }
		  else {
              DialogBox.showMessageDialog("No change. Content not saved.");
		  }
	   }
	}	

	@FXML
	private void onRefresh() {

        final String zkAddress = zkConnectString.getEditor().getText();
        GuiUtils.disableControls(newButton, cloneButton, deleteButton, onRefresh, importButton, exportButton, restoreButton, saveButton, findButton, findNextButton, zkTree, content);
        statLabel.setText("Refreshing content from " + zkAddress + "...");
        refreshTree();
        statLabel.setText("Content from " + zkAddress + " had been refreshed.");
	}
	
	@FXML
	private void onDelete() {
		if (pathInTree!=INVALID) {
	       if (DialogBox.showConfirmationDialog("Do you want to delete node `"+pathInTree+"` and all nodes underneath?")==DialogResult.YES) {
		       adapter.deleteNode(pathInTree);
		       pathInTree=INVALID;
		       refreshTree();
		       refreshContent();		     
	       }
		}		
	}
			
	@FXML
	private void onConnect() {
        connectButton.setDisable(true);
        final String zkAddress = zkConnectString.getEditor().getText();
        statLabel.setText("Connecting to " + zkAddress + "...");
        if (adapter!=null) {
            adapter.disconnect();
        }
        adapter = new ZooKeeperAdapter(zkAddress, new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
               if (connectionState == ConnectionState.CONNECTED || connectionState == ConnectionState.RECONNECTED) {
                   refreshTree();
                   statLabel.setText("Restored connection to ZooKeeper at " + zkAddress + ".");
               } else if (connectionState == ConnectionState.LOST || connectionState == ConnectionState.SUSPENDED) {
                   GuiUtils.disableControls(newButton, cloneButton, deleteButton, onRefresh, importButton, exportButton, restoreButton, saveButton, findButton, findNextButton, zkTree, content);
                   statLabel.setText("Lost connection to ZooKeeper at " + zkAddress + ". Please connect again.");
               }
            }
        });
        new Thread(new Task() {

            @Override
            protected Object call() throws Exception {
                adapter.init();
                if (adapter.isReady()) {
                    refreshTree();
                    statLabel.setText("Connected to ZooKeeper at " + zkConnectString.getEditor().getText() + ".");
                } else {
                    statLabel.setText("ZooKeeper connection towards " + zkConnectString.getEditor().getText() + " got timed out.");
                }


                    connectButton.setDisable(false);
                return null;
            }
        }).run();

		if (!zkConnectString.getItems().contains(zkAddress)) {
			zkConnectString.getItems().add(zkAddress);
		}

	}
	
	private void refreshContent() {
	   if (pathInTree==INVALID) {
	       content.setText("");
	       content.setDisable(true);
	   } else {
		   content.setText(adapter.getNodeData(pathInTree));
		   content.setDisable(false);
	   }
	   contentChange.setVisible(false);
	}
	
	private void refreshTree() {

        int lastSelection = zkTree.getSelectionModel().getSelectedIndex();
		zkTree.setRoot(createTree("", adapter.getChildren("/")));
		pathInTree=INVALID;
		zkTree.getSelectionModel().select(lastSelection);
        GuiUtils.enableControls(newButton, cloneButton, deleteButton, onRefresh, importButton, exportButton, restoreButton, saveButton, findButton, findNextButton, zkTree, content);

	}
	
	private TreeItem<ZooKeeperNode> createTree(String path, List<String> elementsUnderneath) {
		
		TreeItem<ZooKeeperNode> node=new TreeItem<ZooKeeperNode>(new ZooKeeperNode(path));
		
		for (String s: elementsUnderneath) {
			String siblingPath=path + "/" + s;		    
			node.getChildren().add(createTree(siblingPath, adapter.getChildren(siblingPath)));			
		}
		return node;
	}

    @FXML
    public void onFind(ActionEvent actionEvent) {
        final String findText = DialogBox.showQueryDialog("Find substring:", lastSearchQuery);
        lastSearchQuery = findText;

        if (findText != null || findText != "") {
            findStringInContentAndSelectIfFound(lastSearchQuery);
        }
    }

    @FXML
    public void onFindNext(ActionEvent event) {

        if (lastSearchQuery.isEmpty()) {
            DialogBox.showMessageDialog("There was no previous search or it was empty.");
        } else {
            findStringInContentAndSelectIfFound(lastSearchQuery);
        }

    }

    private void findStringInContentAndSelectIfFound(final String findText) {
        String contentStr = content.getText();

        final int nextMatch = contentStr.indexOf(findText,content.getCaretPosition());
        if (nextMatch == -1) {
            DialogBox.showMessageDialog("Text `"+findText+"` could not be found starting from caret position.");
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    content.selectRange(nextMatch, nextMatch + findText.length());
                }
            });

        }

    }
}
