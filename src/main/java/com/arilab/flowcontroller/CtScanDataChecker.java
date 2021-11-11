package com.arilab.flowcontroller;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.system.SystemExit;
import com.arilab.utils.Config;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class CtScanDataChecker {

    private static Logger logger = LoggerFactory.getLogger(CtScanDataChecker.class);

    private FileUtils fileUtils;
    private Config config;
    private SystemExit systemExit;

    public CtScanDataChecker(FileUtils fileUtils, Config config, SystemExit systemExit) {
        this.fileUtils = fileUtils;
        this.config = config;
        this.systemExit = systemExit;
    }

    public void check(CtScanCollection ctScanCollection) {
        List<CtScan> scanList = ctScanCollection.getCtScans();

        int validScans = 0;
        for (CtScan ctScan: scanList) {
            if (ctScan.getAllinputDataIsValid()) {
                validScans += 1;
            }
        }

        if (validScans != scanList.size()) {
            logger.error("Not all scans passed validation of input data, migration will not proceed. Please see the" +
                    " file " + config.getFailedOutputFile() + " for further details.");
            fileUtils.writeBeansToFile(ctScanCollection, config.getFailedOutputFile());
            systemExit.exit(1);
        }
    }



}
