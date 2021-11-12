package com.arilab.utils;

import java.io.File;
import java.io.IOException;

public interface DirectoryMover {
    public void move(File sourceDir, File destinationDir) throws IOException;
}
