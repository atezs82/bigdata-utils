package org.zenworks.redis.buddy;

import java.io.IOException;

import javafx.scene.image.Image;
import org.zenworks.common.Common;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zenworks.common.module.FrameworkModule;

public class MainWindow extends Application implements FrameworkModule {
	
	public static void main(String[] args) {
		Common.initConfig(args);
		launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
    	Scene scene = new Scene(getInterface());
    
        stage.setTitle(getName());
        stage.getIcons().add(getIcon());
        stage.setScene(scene);        
        stage.show();
    }
    
    @Override
    public Parent getInterface() {
        try {
            return FXMLLoader.load(new MainWindow().getClass().getResource("/redis-buddy.fxml"));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Image getIcon() {
        return new Image(getClass().getResourceAsStream("/redisbuddyicon.png"));
    }

    @Override
    public String getName() {
        return "Redis Buddy";
    }
}
