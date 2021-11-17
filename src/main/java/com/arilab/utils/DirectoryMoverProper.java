package com.arilab.utils;

import java.io.File;
import java.io.IOException;

public class DirectoryMoverProper implements DirectoryMover {

    @Override
    public void move(File sourceDir, File destinationDir) throws IOException {
        org.apache.commons.io.FileUtils.moveDirectory(sourceDir, destinationDir);
    }
}
