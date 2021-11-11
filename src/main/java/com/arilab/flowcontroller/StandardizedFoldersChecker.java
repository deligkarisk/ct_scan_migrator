package com.arilab.flowcontroller;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.system.SystemExit;
import com.arilab.utils.Config;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StandardizedFoldersChecker {

    private static Logger logger = LoggerFactory.getLogger(StandardizedFoldersChecker.class);

    private SystemExit systemExit;
    private FileUtils fileUtils;


    public StandardizedFoldersChecker(SystemExit systemExit, FileUtils fileUtils) {
        this.systemExit = systemExit;
        this.fileUtils = fileUtils;
    }

    public void check(CtScanCollection ctScanCollection, String failedOutputFile) {
        List<CtScan> scanList = ctScanCollection.getCtScans();

        for (CtScan ctScan : scanList) {
            if (!(ctScan.getNewFolderPathAvailable() && ctScan.getNewFolderPathAvailableIntheDatabase())) {
                logger.error("Not all scans passed the validation of the standardized folder names. See the " +
                        failedOutputFile + "file for details.");
                fileUtils.writeBeansToFile(ctScanCollection, failedOutputFile);
                systemExit.exit(1);
            }

        }
    }
}
