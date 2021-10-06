package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.utils.Config;
import com.arilab.utils.CtScanUtils;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class CtScanUtilsService {

    private CtScanUtils ctScanUtils;
    private CtScanValidator ctScanValidator;
    private FileUtils fileUtils;
    private CtScanValidatorService ctScanValidatorService;
    private Config config;
    private CTScanService ctScanService;

    public CtScanUtilsService(CtScanUtils ctScanUtils, CtScanValidator ctScanValidator, FileUtils fileUtils, CtScanValidatorService ctScanValidatorService, Config config, CTScanService ctScanService) {
        this.ctScanUtils = ctScanUtils;
        this.ctScanValidator = ctScanValidator;
        this.fileUtils = fileUtils;
        this.ctScanValidatorService = ctScanValidatorService;
        this.config = config;
        this.ctScanService = ctScanService;
    }

    private static final Logger logger = LoggerFactory.getLogger(CtScanUtilsService.class);





    public void findStandardizedFolderNames(List<CtScan> scanList) {
        Iterator<CtScan> ctScanIterator = scanList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ctScanUtils.findStandardizedFolderName(ctScan);
        }
    }


}
