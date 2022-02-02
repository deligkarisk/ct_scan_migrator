package com.arilab.service;

import com.arilab.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemService {

    private Config config;

    public FileSystemService(Config config) {
        this.config = config;
    }

    Logger logger = LoggerFactory.getLogger(FileSystemService.class);


    public void filesystemCheck() throws IOException {
        Boolean sourceFolderIsMounted = isMounted(stringToPath(config.getSourceDirectory()));
        Boolean targetFolderIsMounted = isMounted(stringToPath(config.getTargetDirectory()));

        if (!(sourceFolderIsMounted && targetFolderIsMounted)) {
            logger.error("Bucket folders are not mounted, aborting operations...");
            throw new IOException("Bucket folders are not mounted, aborting operations...");
        }

        Boolean targetFolderIsWritable = isWriteable(stringToPath(config.getTargetDirectory()));
        if (!targetFolderIsWritable) {
            logger.error("Cannot write to new bucket location, aborting...");
            throw new IOException("Target folder is not writeable, aborting operations...");
        }
    }



    private Boolean isMounted(Path path) {
        return Files.exists(path);
    }

    private Boolean isWriteable(Path path) {
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

    private Path stringToPath(String string) {
        return Paths.get(string);
    }
}
