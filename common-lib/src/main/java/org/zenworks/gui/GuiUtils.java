package org.zenworks.gui;

import javafx.scene.Node;

public class GuiUtils {

    public static void enableControls(Node... nodes) {
        for (Node n:nodes) {
            n.setDisable(false);
        }
    }

    public static void disableControls(Node... nodes) {
        for (Node n:nodes) {
            n.setDisable(true);
        }
    }

}
