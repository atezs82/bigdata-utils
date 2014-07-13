package org.zenworks.bigdata.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.zenworks.common.Common;
import org.zenworks.common.config.Config;

public class MainWindow extends Application {

	@Override
	public void start(Stage arg0) throws Exception {
		
        Parent root = FXMLLoader.load(getClass().getResource("/main-window.fxml"));    	
        
        Scene scene = new Scene(root);
    
        arg0.setTitle("Bigdata Util Framework");
        arg0.getIcons().add(new Image(getClass().getResourceAsStream("/frameworkmainicon.png")));
        arg0.setScene(scene);

        arg0.show();
        
        
    		
	}

	public static void main(String[] args) {
        Common.initConfig(args);
        launch(args);

	}

}
