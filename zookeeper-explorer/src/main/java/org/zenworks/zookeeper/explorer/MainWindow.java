package org.zenworks.zookeeper.explorer;

import java.io.IOException;

import org.zenworks.common.Common;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.zenworks.common.module.FrameworkModule;

public class MainWindow extends Application implements FrameworkModule {
		
	public static void main(String[] args) {
		Common.initConfig(args);
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
    	Parent root = getInterface();
        
        Scene scene = new Scene(root);
    
        stage.setTitle("Zookeeper explorer");
        stage.getIcons().add(getIcon());
        stage.setScene(scene);        
        stage.show();
    }
    
    public Parent getInterface() {
    	try {
    		return FXMLLoader.load(new MainWindow().getClass().getResource("/window.fxml"));    	
        } catch (IOException exc) {
    		return null;
    	}
    }

    public Image getIcon() {
        return new Image(getClass().getResourceAsStream("/zookeeperexplorericon.png"));
    }

    @Override
    public String getName() {
        return "Zookeeper Explorer";
    }
}
