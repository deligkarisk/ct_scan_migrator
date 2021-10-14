package com.arilab.flowcontroller;

import com.arilab.system.SystemExit;
import com.arilab.utils.Config;
import com.arilab.utils.FileSystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesystemConnectivityChecker {

    private static Logger logger = LoggerFactory.getLogger(FilesystemConnectivityChecker.class);

    Config config;
    SystemExit systemExit;
    FileSystemUtils fileSystemUtils;

    public FilesystemConnectivityChecker(Config config, SystemExit systemExit, FileSystemUtils fileSystemUtils) {
        this.config = config;
        this.systemExit = systemExit;
        this.fileSystemUtils = fileSystemUtils;
    }

    public void check() {

        int checkResult = internalCheckFunction();

        if (checkResult == 1) {
            systemExit.exit(1);
        }


    }

    private int internalCheckFunction() {
        Boolean sourceFolderIsMounted =
                fileSystemUtils.isMounted(fileSystemUtils.stringToPath(config.getSourceDirectory()));
        Boolean targetFolderIsMounted = fileSystemUtils.isMounted(fileSystemUtils.stringToPath(config.getTargetDirectory()));

        if (!(sourceFolderIsMounted && targetFolderIsMounted)) {
            logger.error("Bucket folders are not mounted, aborting operations...");
           return 1;
        }

        Boolean targetFolderIsWritable = fileSystemUtils.isWriteable(fileSystemUtils.stringToPath(config.getTargetDirectory()));
        if (!targetFolderIsWritable) {
            logger.error("Cannot write to new bucket location, aborting...");
            return 1;
        }

        return 0;
    }
}

