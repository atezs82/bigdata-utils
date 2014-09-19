package org.zenworks.gui;

/**
 * Created by atezs82 on 9/13/14.
 */
public interface TextProgressCallback {

    void appendProgress(final String progress);

    void setPercentage(final double percentage);

    void complete();

}
