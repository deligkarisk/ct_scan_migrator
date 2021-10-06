package com.arilab.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemUtils {


    public Boolean isMounted(Path path) {
        return Files.exists(path);
    }

    public Boolean isWriteable(Path path) {

        Boolean isWriteable = true;

        Path testFilePath = Paths.get(path.toString(), "testfile");
        try {
            Files.deleteIfExists(testFilePath);
            Files.createFile(testFilePath);
            Files.deleteIfExists(testFilePath);
        } catch (IOException e) {
            isWriteable = false;
        }
        return isWriteable;
    }

    public Path stringToPath(String string) {
        return Paths.get(string);
    }
}
