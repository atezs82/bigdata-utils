package org.zenworks.common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by atezs82 on 9/10/14.
 */
public class FileLister {

    public List<String> getListOfFiles(final String sourceDir) {

        List<String> result = new ArrayList<String>();

        for (File file : (Collection<File>)FileUtils.listFiles(new File(sourceDir), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)) {

            if (file.isDirectory()) {
                result.addAll(getListOfFiles(file.getAbsolutePath()));
            } else {
                result.add(file.getAbsolutePath());
            }

        }

        return result;

    }

}
