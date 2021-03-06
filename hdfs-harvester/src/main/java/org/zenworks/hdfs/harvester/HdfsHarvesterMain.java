package org.zenworks.hdfs.harvester;

import java.io.IOException;

import org.zenworks.common.Common;
import org.zenworks.common.module.FrameworkModule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class HdfsHarvesterMain extends Application implements FrameworkModule {
	
	public static void main(String[] args) {
        Common.initConfig(args);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(getInterface());

        stage.setTitle(getName());
        stage.getIcons().add(getIcon());
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public Parent getInterface() {
        try {
            return FXMLLoader.load(new HdfsHarvesterMain().getClass().getResource("/hdfs-harvester.fxml"));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Image getIcon() {
       return new Image(getClass().getResourceAsStream("/hdfsharasser.png"));
    }

    @Override
    public String getName() {
        return "HDFS Harvester";
    }

}
