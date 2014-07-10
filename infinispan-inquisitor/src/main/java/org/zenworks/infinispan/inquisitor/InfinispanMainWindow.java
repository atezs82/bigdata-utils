package org.zenworks.infinispan.inquisitor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.zenworks.common.Common;

import java.io.IOException;

public class InfinispanMainWindow extends Application {

    public static void main(String[] args) {
        Common.initConfig(args);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(create());

        stage.setTitle("Infinispan Inquisitor");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static Parent create() {
        try {
            return FXMLLoader.load(new InfinispanMainWindow().getClass().getResource("/ipan.fxml"));
        } catch (IOException e) {
            return null;
        }

    }

}
