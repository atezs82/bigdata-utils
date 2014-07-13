package org.zenworks.infinispan.inquisitor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.zenworks.common.Common;
import org.zenworks.common.module.FrameworkModule;

import java.io.IOException;

public class InfinispanMainWindow extends Application implements FrameworkModule {

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
            return FXMLLoader.load(new InfinispanMainWindow().getClass().getResource("/ipan.fxml"));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Image getIcon() {
        return new Image(getClass().getResourceAsStream("/infinispaninquisitoricon.png"));
    }

    @Override
    public String getName() {
        return "Infinispan Inquisitor";
    }
}
