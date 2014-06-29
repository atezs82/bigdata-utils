package org.zenworks.redis.buddy;

import java.io.IOException;

import org.zenworks.common.Common;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {
	
	public static void main(String[] args) {
		Common.initConfig(args);
		launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
    	Scene scene = new Scene(create());
    
        stage.setTitle("Redis buddy");
        stage.setScene(scene);        
        stage.show();
    }
    
    public static Parent create() {
    	try {
			return FXMLLoader.load(new MainWindow().getClass().getResource("/redis-buddy.fxml"));
		} catch (IOException e) {
		   return null;
		}    	
        
    }

}
