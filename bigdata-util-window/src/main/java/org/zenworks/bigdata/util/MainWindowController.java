package org.zenworks.bigdata.util;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.zenworks.common.config.Config;
import org.zenworks.common.module.FrameworkModule;
import org.zenworks.infinispan.inquisitor.InfinispanMainWindow;
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
        FrameworkModule[] modules = new FrameworkModule[]{ new MainWindow(), new org.zenworks.redis.buddy.MainWindow(), new InfinispanMainWindow() };

        for (FrameworkModule fm : modules) {
            tabPane.getTabs().add(createTab(fm.getName(), fm.getIcon(), fm.getInterface()));
        }

	}
	
	private Tab createTab(final String caption, final Image icon, final Parent content) {
		Tab tab = new Tab();
		tab.setText(caption);
        ImageView iconView = new ImageView(icon);
        iconView.setPreserveRatio(true);
        iconView.setFitWidth(20.0);
        tab.setGraphic(iconView);
	    tab.setContent(content);

		return tab;
	}
	
}
