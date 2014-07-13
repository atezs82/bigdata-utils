package org.zenworks.common.module;

import javafx.scene.Parent;
import javafx.scene.image.Image;

public interface FrameworkModule {

    Parent getInterface();

    Image getIcon();

    String getName();

}
