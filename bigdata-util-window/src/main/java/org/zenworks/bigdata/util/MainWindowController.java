package org.zenworks.bigdata.util;

import java.net.URL;
import java.util.ResourceBundle;

import org.zenworks.zookeeper.explorer.MainWindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainWindowController implements Initializable {

	@FXML
	TabPane tabPane;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		tabPane.getTabs().add(createTab("ZooKeeper", MainWindow.create()));
		tabPane.getTabs().add(createTab("Redis", org.zenworks.redis.buddy.MainWindow.create()));
		tabPane.getTabs().add(createTab("Infinispan", new Label("Yet to come")));
		tabPane.getTabs().add(createTab("Storm", new Label("Yet to come")));
	}
	
	private Tab createTab(final String caption, final Parent content) {
		Tab tab = new Tab();
		tab.setText(caption);		
	    tab.setContent(content);
		
		return tab;
	}
	
}
