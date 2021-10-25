package com.arilab.flowcontroller;

import com.arilab.domain.CtScan;
import com.arilab.system.SystemExit;
import com.arilab.utils.Config;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StandardizedFoldersChecker {

    private static Logger logger = LoggerFactory.getLogger(StandardizedFoldersChecker.class);


    private Config config;
    private SystemExit systemExit;
    private FileUtils fileUtils;


    public StandardizedFoldersChecker(Config config, SystemExit systemExit, FileUtils fileUtils) {
        this.config = config;
        this.systemExit = systemExit;
        this.fileUtils = fileUtils;
    }

    public void check(List<CtScan> ctScanList) {
        for (CtScan ctScan : ctScanList) {
            if (!(ctScan.getNewFolderPathAvailable() && ctScan.getNewFolderPathAvailableIntheDatabase())) {
                logger.error("Not all scans passed the validation of the standardized folder names. See the " +
                                config.getFailedOutputFile() + "file for details.");
                fileUtils.writeBeansToFile(ctScanList, config.getFailedOutputFile());
                systemExit.exit(1);
            }

        }
    }
}
