package org.zenworks.zookeeper.explorer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javafx.scene.control.*;
import org.apache.commons.io.FileUtils;
import org.zenworks.common.Common;
import org.zenworks.common.ConfigKey;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.zenworks.gui.GuiUtils;

public class MainWindowController implements Initializable {

	@FXML
	private TreeView<ZooKeeperNode> zkTree;
	
	@FXML
	private TextArea content;
	
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
		
	}
	
		
	@FXML
	private void onNew() {
	   String root = (pathInTree==INVALID?"/":pathInTree+"/");	   
	   adapter.createNode(root+JOptionPane.showInputDialog(null, "Enter the name of the node to create under `"+root+"`:"),"");	   
	   refreshTree();	   
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
			 JOptionPane.showMessageDialog(null, importFile.getAbsolutePath() + " was imported to " + targetPath);
			 refreshTree();			 
		 } catch (IOException exc) {
			 JOptionPane.showMessageDialog(null, "Could not import file.");
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
				 JOptionPane.showMessageDialog(null, pathInTree + " was exported to " + exportFile.getAbsolutePath());
			 } catch (IOException exc) {
				 JOptionPane.showMessageDialog(null, "Could not export file.");
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
			if (JOptionPane.showConfirmDialog(null, "Do you want to restore the original content of `"+pathInTree+"`?", "Confirmation", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
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
			  JOptionPane.showMessageDialog(null, "Saved " + content.getText().getBytes().length + " bytes to " + pathInTree);			 
		  }
		  else {
			  JOptionPane.showMessageDialog(null, "No change. Content not saved.");
		  }
	   }
	}	

	@FXML
	private void onRefresh() {
		refreshTree();		
	}
	
	@FXML
	private void onDelete() {
		if (pathInTree!=INVALID) {
	       if (JOptionPane.showConfirmDialog(null, "Do you want to delete node `"+pathInTree+"` and all nodes underneath?", "Confirmation", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
		       adapter.deleteNode(pathInTree);
		       pathInTree=INVALID;
		       refreshTree();
		       refreshContent();		     
	       }
		}		
	}
			
	@FXML
	private void onConnect() {	
		adapter = new ZooKeeperAdapter(zkConnectString.getEditor().getText());
		if (!zkConnectString.getItems().contains(zkConnectString.getEditor().getText())) {
			zkConnectString.getItems().add(zkConnectString.getEditor().getText());
		}				
		refreshTree();
        GuiUtils.enableControls(newButton, cloneButton, deleteButton, onRefresh, importButton, exportButton, restoreButton, saveButton);
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
	}
	
	private TreeItem<ZooKeeperNode> createTree(String path, List<String> elementsUnderneath) {
		
		TreeItem<ZooKeeperNode> node=new TreeItem<ZooKeeperNode>(new ZooKeeperNode(path));
		
		for (String s: elementsUnderneath) {
			String siblingPath=path + "/" + s;		    
			node.getChildren().add(createTree(siblingPath, adapter.getChildren(siblingPath)));			
		}
		return node;
	}

}
