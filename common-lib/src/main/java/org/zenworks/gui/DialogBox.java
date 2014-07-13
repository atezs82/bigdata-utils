package org.zenworks.gui;

import javafx.scene.image.Image;

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

    public static String showQueryDialog(final String message) {
        TextQueryDialog queryDialog = new TextQueryDialog();
        return queryDialog.show(message, getRandomImage(), getRandomImage());
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
