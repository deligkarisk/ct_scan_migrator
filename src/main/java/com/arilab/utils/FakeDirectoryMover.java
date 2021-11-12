package com.arilab.utils;

import java.io.File;
import java.io.IOException;

public class FakeDirectoryMover implements DirectoryMover{

    public File calledSourceDir;
    public File calledDestDir;

    @Override
    public void move(File sourceDir, File destinationDir) throws IOException {
        System.out.println("Moving directory from: " + sourceDir.toString() + " to: " + destinationDir.toString());
        calledDestDir = destinationDir;
        calledSourceDir = sourceDir;


    }
}
