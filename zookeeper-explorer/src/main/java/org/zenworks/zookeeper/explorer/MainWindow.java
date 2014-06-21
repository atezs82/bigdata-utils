package org.zenworks.zookeeper.explorer;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class MainWindow extends Application {
		
	public static void main(String[] args) {
		for (String s:args) {
			if (s.startsWith("--open=")) {
				Config.zkConnectString=s.split("=")[1].split(";");
			}
		}
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/window.fxml"));    	
        
        Scene scene = new Scene(root);
    
        stage.setTitle("Zookeeper explorer");
        stage.setScene(scene);        
        stage.show();
    }
    
    public static Parent create() {
    	try {
    		return FXMLLoader.load(new MainWindow().getClass().getResource("/window.fxml"));    	
        } catch (IOException exc) {
    		return null;
    	}
    
    }
}
