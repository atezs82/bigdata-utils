package org.zenworks.zookeeper.explorer;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class ZooKeeperTreeItem extends TreeCell<ZooKeeperNode> {
	
	private TextField textField;
	
	private Callback<String, Object> nameCallback;
	 
    public ZooKeeperTreeItem(Callback<String, Object> newNameCallback) {
       this.nameCallback = newNameCallback;
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem().getName());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    protected void updateItem(ZooKeeperNode arg0, boolean arg1) {
    	
    	super.updateItem(arg0, arg1);
    	
    	 if (arg1) {
             setText(null);
             setGraphic(null);
         } else {
             if (isEditing()) {
                 if (textField != null) {
                     textField.setText(getString());
                 }
                 setText(null);
                 setGraphic(textField);
             } else {
                 setText(getString());
                 setGraphic(getTreeItem().getGraphic());
             }
         }
    }
    
    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(new ZooKeeperNode(textField.getText()));
                    nameCallback.call(textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

}
