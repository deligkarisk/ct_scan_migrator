package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class CtScanValidatorService {

    private static Logger logger = LoggerFactory.getLogger(CtScanValidatorService.class);

    CtScanValidator ctScanValidator = new CtScanValidator();
    FileUtils fileUtils = new FileUtils();


    public void validateStandardizedFolderNames(List<CtScan> ctScanList, String FAILED_FILE_TO_WRITE) {
        int validScans;
        validScans = validateStandardizedFolderNames(ctScanList);
        if (validScans != ctScanList.size()) {
            logger.error("Not all scans passed validation of new standardized folder, migration will not proceed. " +
                                 "Please see the" +
                                 "file " + FAILED_FILE_TO_WRITE + " for further details.");
            fileUtils.writeBeansToFile(ctScanList, FAILED_FILE_TO_WRITE );
            System.exit(1);
        }
    }

    private int validateStandardizedFolderNames(List<CtScan> ctScanList) {
        int validCount = 0;
        Iterator<CtScan> ctScanIterator = ctScanList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            logger.info("Validating derived standardized folder " + ctScan.getNewFolderPath());
            Boolean currentScanValid = ctScanValidator.validateStandardizedFolder(ctScan);
            if (currentScanValid) {validCount += 1;}
        }
        return validCount;
    }


    public void validateScanData(List<CtScan> scansList, String FAILED_FILE_TO_WRITE) {
        int validScans;
        validScans = validateScanData(scansList); // validates if scans can be migrated, all
        // info is correct.
        if (validScans != scansList.size()) {
            logger.error("Not all scans passed validation of input data, migration will not proceed. Please see the" +
                                 "file " + FAILED_FILE_TO_WRITE + " for further details.");
            fileUtils.writeBeansToFile(scansList, FAILED_FILE_TO_WRITE );
            System.exit(1);
        }
    }

    // returns the number of scans passing the validation
    private int validateScanData(List ctScanList) {
        int validCount = 0;
        Iterator<CtScan> ctScanIterator = ctScanList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            logger.info("Validating scan " + ctScan.getSpecimenCode() + ", " + ctScan.getFolderLocation());
            Boolean currentScanValid = ctScanValidator.validateInputData(ctScan);
            if (currentScanValid) {validCount += 1;}
        }
        return validCount;
    }
}
