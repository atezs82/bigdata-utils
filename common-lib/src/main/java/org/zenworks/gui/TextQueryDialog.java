package org.zenworks.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TextQueryDialog {

    String result = null;

    public String show(final String message, final String initialContent, final Image image1, final Image image2) {

        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Button okButton = new Button("Ok");
        Button cancelButton = new Button("Cancel");
        final TextField inputField = new TextField();
        inputField.setText(initialContent);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                dialogStage.close();
                result=inputField.getText();
            }
        });
        cancelButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                dialogStage.close();
                result=inputField.getText();
            }
        });
        ImageView imageView1 = new ImageView(image1);
        imageView1.setPreserveRatio(true);
        imageView1.setFitWidth(64.0);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setPreserveRatio(true);
        imageView2.setFitWidth(64.0);

        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(HBoxBuilder.create().children(imageView1, new Text(message), inputField, imageView2).alignment(Pos.CENTER).spacing(5.0).padding(new Insets(25)).build(),
                        HBoxBuilder.create().children(okButton,cancelButton).spacing(5.0).alignment(Pos.CENTER).build()).
                alignment(Pos.CENTER).padding(new Insets(5)).build()));
        dialogStage.setResizable(false);
        dialogStage.setTitle("Query");
        dialogStage.showAndWait();

        return result;
    }

}
