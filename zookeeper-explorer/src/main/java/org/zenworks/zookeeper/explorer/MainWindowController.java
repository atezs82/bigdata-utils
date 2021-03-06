package org.zenworks.zookeeper.explorer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.zenworks.common.Common;
import org.zenworks.common.FileLister;
import org.zenworks.common.config.ConfigKey;
import org.zenworks.gui.DialogBox;
import org.zenworks.gui.DialogResult;
import org.zenworks.gui.GuiUtils;
import org.zenworks.gui.TextProgressCallback;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

// TODO do startscripts for all components
// TODO compile without javafx system dependency in POM
// TODO ComboBox change does not make the tree change (not always)
// TODO combobox change or refresh press shall enable the connect button again
// TODO Dialógusablak a GUI mögé megy néha, nem lehet látni az ablakot a nagyobb ablaktól.
// TODO import and export directories functionality
public class MainWindowController implements Initializable {

    final static String INVALID = "";
    String pathInTree = INVALID;
    private final String EMPTY = "";
    private String lastSearchQuery = EMPTY;
    private final long ZK_MAX_ALLOWED = 1024*1024;
    ZooKeeperAdapter adapter = null;
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
    @FXML
    private TreeView<ZooKeeperNode> zkTree;
    @FXML
    private TextArea content;
    @FXML
    private Button connectButton;
    @FXML
    private ComboBox<String> zkConnectString;
    @FXML
    private ImageView contentChange;
    private double actualFontSize = Font.getDefault().getSize();
    private double ZOOM_FACTOR = 1.25;
    private int MAX_ZOOM_COUNT = 5;

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
        zkConnectString.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    onConnect();
                }
            }
        });

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
         fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		 fileChooser.showDialog(null, "Import file or directory");
         final File importFile = fileChooser.getSelectedFile();
         if (importFile.isDirectory()) {
             final List<String> files = new FileLister().getListOfFiles(importFile.getAbsolutePath());
             final String targetPath = (pathInTree == INVALID ? "/" : pathInTree);

             if (DialogBox.showConfirmationDialog("Are you sure you want to import all " + files.size() + " files from under " + importFile.getAbsolutePath() + " to ZooKeeper path " + targetPath) == DialogResult.YES) {
                 final TextProgressCallback importProgress = DialogBox.showTextProgressDialog("Import progress");
                 new Thread() {
                     @Override
                     public void run() {

                         int filesImported=0;
                         for (String file:files) {
                             String pathWithZookeeperSlashes = file.replaceAll("\\\\","/").substring(importFile.getAbsolutePath().length() + 1, file.length());
                             try {
                                 importProgress.setPercentage((double)filesImported / (double)files.size());
                                 filesImported += 1;
                                 if (FileUtils.sizeOf(new File(file)) < ZK_MAX_ALLOWED) {
                                     byte[] fileContent = FileUtils.readFileToByteArray(new File(file));

                                     importProgress.appendProgress("Importing " + pathWithZookeeperSlashes + " to under " + targetPath + " (" + fileContent.length + " bytes read)...");
                                     adapter.createNode(targetPath + "/" + pathWithZookeeperSlashes, fileContent);
                                 } else {
                                     importProgress.appendProgress("Could import " + pathWithZookeeperSlashes + ", file is too big (length is " + FileUtils.sizeOf(new File(file)) + " bytes). Maximum allowed is "+ZK_MAX_ALLOWED+" bytes.");
                                 }
                             } catch (IOException exc) {
                                 DialogBox.showConfirmationDialog("Could not import file.");
                             }
                         }

                         importProgress.appendProgress("Imported all " + files.size() + " files to " + targetPath + ".");
                         importProgress.setPercentage(100.0);
                         importProgress.complete();
                     }
                 }.start();
                 refreshTree();

             }

         } else {
             try {
                 byte[] fileContent = FileUtils.readFileToByteArray(importFile);
                 String importNode = JOptionPane.showInputDialog(null, "What shall be the name of the new node under `" + (pathInTree == INVALID ? "/" : pathInTree) + "`?");
                 String targetPath = (pathInTree == INVALID ? "/" + importNode : pathInTree + "/" + importNode);
                 adapter.createNode(targetPath, fileContent);
                 DialogBox.showConfirmationDialog(importFile.getAbsolutePath() + " was imported to " + targetPath);
                 refreshTree();
             } catch (IOException exc) {
                 DialogBox.showConfirmationDialog("Could not import file.");
             }
         }
	}

	@FXML
	private void onExport() {
		 JFileChooser fileChooser = new JFileChooser();
         fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		 fileChooser.showDialog(null, "Export to directory");
		 final File exportFile = fileChooser.getSelectedFile();

         if (pathInTree != INVALID) {
             if ((DialogBox.showConfirmationDialog("Are you sure you want to export all content from under " + pathInTree + " to directory " + exportFile.toString())) == DialogResult.YES) {
                 final TextProgressCallback exportProgress = DialogBox.showTextProgressDialog("Export progress");
                 final List<String> nodes = adapter.getNodeAndChildren(pathInTree);
                 new Thread() {
                     @Override
                     public void run() {

                         for (String node : nodes) {
                             try {
                                 String filePathUnderSelectedDirectory = node.replaceAll("/", File.separator);
                                 byte[] nodeData = adapter.getNodeDataBytes(node);
                                 exportProgress.appendProgress("Exporting node " + node + " to file " + exportFile.getAbsolutePath() + filePathUnderSelectedDirectory + " (" + nodeData.length + " bytes)...");
                                 FileUtils.writeByteArrayToFile(new File(exportFile.getAbsolutePath() + filePathUnderSelectedDirectory), nodeData);
                             } catch (IOException exc) {
                                 DialogBox.showMessageDialog("Could not export file.");
                                 exc.printStackTrace();
                             }
                         }
                     }
                 }.run();
                 exportProgress.appendProgress(pathInTree + " was exported to directory " + exportFile.getAbsolutePath() + " (" + nodes.size() + " altogether exported).");
                 exportProgress.complete();
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statLabel.setText("Connecting to " + zkAddress + "...");
            }
        });
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

        if (findText != null || !findText.equals(EMPTY)) {
            findStringInContentAndSelectIfFound(lastSearchQuery);
        }
    }

    @FXML
    public void onFindNext(ActionEvent event) {

        if (lastSearchQuery.equals(EMPTY)) {
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

    public void onZoomIn(ActionEvent actionEvent) {
        actualFontSize = (actualFontSize >= Font.getDefault().getSize() * Math.pow(ZOOM_FACTOR, MAX_ZOOM_COUNT) ? actualFontSize : actualFontSize * ZOOM_FACTOR);
        content.setStyle("-fx-font-size: " + actualFontSize + ";");
    }

    public void onZoomOut(ActionEvent actionEvent) {
        actualFontSize = (actualFontSize <= Font.getDefault().getSize() * Math.pow(1.0 / ZOOM_FACTOR, MAX_ZOOM_COUNT) ? actualFontSize : actualFontSize / ZOOM_FACTOR);
        content.setStyle("-fx-font-size: " + actualFontSize + ";");
    }

    public void onZoomDefault(ActionEvent actionEvent) {
        actualFontSize = Font.getDefault().getSize();
        content.setStyle("-fx-font-size: " + actualFontSize + ";");
    }
}
