package org.zenworks.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.HBoxBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by atezs82 on 9/12/14.
 */
public class TextProgressDialog {

    public TextProgressCallback show(final String caption, final Image image1) {

        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        final Button okButton = new Button("Ok");
        final TextArea progressContent = new TextArea();
        progressContent.setPrefSize(300.0,200.0);
        progressContent.setEditable(false);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                dialogStage.close();
            }
        });
        okButton.setDisable(true);

        ImageView imageView1 = new ImageView(image1);
        imageView1.setPreserveRatio(true);
        imageView1.setFitWidth(64.0);

        final ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0.0);
        progressBar.setMaxWidth(Double.MAX_VALUE);

        BorderPane iconWithContent = new BorderPane();
        iconWithContent.setLeft(imageView1);
        iconWithContent.setCenter(progressContent);
        iconWithContent.setBottom(progressBar);

        dialogStage.setScene(new Scene(BorderPaneBuilder.create().
                left(imageView1).
                center(progressContent).
                top(BorderPaneBuilder.create().center(progressBar).build()).
                bottom(HBoxBuilder.create().children(okButton).alignment(Pos.CENTER).build()).build()));

        dialogStage.setResizable(true);
        dialogStage.setTitle(caption);
        dialogStage.show();

        return new TextProgressCallback() {
            @Override
            public void appendProgress(final String progress) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progressContent.setText(progressContent.getText() + progress + "\n");
                    }
                });
            }

            @Override
            public void setPercentage(final double percentage) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(percentage);
                    }
                });

            }

            @Override
            public void complete() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        okButton.setDisable(false);
                    }
                });
            }
        };

    }
}
