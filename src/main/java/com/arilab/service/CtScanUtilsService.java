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

    private CtScanUtils ctScanUtils = new CtScanUtils();
    private CtScanValidator ctScanValidator = new CtScanValidator();
    private FileUtils fileUtils = new FileUtils();
    private CtScanValidatorService ctScanValidatorService = new CtScanValidatorService();
    private Config config = Config.getInstance();
    private CTScanService ctScanService = new CTScanService();

    private static final Logger logger = LoggerFactory.getLogger(CtScanUtilsService.class);





    public void findStandardizedFolderNames(List<CtScan> scanList) {
        Iterator<CtScan> ctScanIterator = scanList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ctScanUtils.findStandardizedFolderName(ctScan);
        }
    }


}
