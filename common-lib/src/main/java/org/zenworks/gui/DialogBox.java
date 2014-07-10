package org.zenworks.gui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogBox {

    public static void showMessageDialog(final String message) {
        MessageDialog msgDlg = new MessageDialog();
        msgDlg.show(message, getRandomImage(), getRandomImage());

    }

    public static DialogResult showConfirmationDialog(final String message) {

        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.showAndWait(message,getRandomImage(), getRandomImage());
        return dialog.getResult();

    }

    public static Image getRandomImage() {
       int randomIndex = (int)(Math.random()*5.0);
       switch(randomIndex) {
           case 0: return new Image(DialogBox.class.getResourceAsStream("/bulldog.png"));
           case 1: return new Image(DialogBox.class.getResourceAsStream("/donkey.png"));
           case 2: return new Image(DialogBox.class.getResourceAsStream("/giraffe.png"));
           case 3: return new Image(DialogBox.class.getResourceAsStream("/rooster.png"));
           default: return new Image(DialogBox.class.getResourceAsStream("/snake.png"));
       }

    }

}
