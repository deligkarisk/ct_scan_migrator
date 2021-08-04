package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.utils.CtScanUtils;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class CtScanUtilsService {

    CtScanUtils ctScanUtils = new CtScanUtils();
    CtScanValidator ctScanValidator = new CtScanValidator();
    FileUtils fileUtils = new FileUtils();
    CtScanValidatorService ctScanValidatorService = new CtScanValidatorService();

    private static final Logger logger = LoggerFactory.getLogger(CtScanUtilsService.class);



    public void preProcessScans(List<CtScan> scanList) {
        Iterator<CtScan> ctScanIterator = scanList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            logger.info("Fixing scan: " + ctScan.getFolderLocation());
            ctScanUtils.updateScanFolderLocation(ctScan);
            ctScanUtils.findDicomFolderLocation(ctScan, 1, "_OUT");
            ctScanUtils.updateTimestamp(ctScan);
        }
    }

    public void findStandardizedFolderNames(List<CtScan> scanList) {
        Iterator<CtScan> ctScanIterator = scanList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ctScanUtils.findStandardizedFolderName(ctScan);
        }
    }


}
