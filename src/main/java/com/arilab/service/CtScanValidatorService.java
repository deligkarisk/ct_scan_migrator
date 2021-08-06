package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CtScanValidatorService {

    private static Logger logger = LoggerFactory.getLogger(CtScanValidatorService.class);

    CtScanValidator ctScanValidator = new CtScanValidator();
    FileUtils fileUtils = new FileUtils();


    public void validateUniquenessOfFolders(List<CtScan> ctScanList, String failedValidationFileOutput) {
        // Ensure that migrated folders, and dicom folders are all unique inside the fill list of scans.
        List<String> ctScanFolders = ctScanList.stream().map(CtScan::getNewFolderPath).collect(Collectors.toList());
        Set<String> setCtScanFolders = new HashSet<String>(ctScanFolders);
        List<String> ctScanDicomFolders = ctScanList.stream().map(CtScan::getDicomFolderLocation).collect(Collectors.toList());
        Set<String> setCtScanDicomFolders = new HashSet<String>(ctScanDicomFolders);

        if ((ctScanFolders.size() != setCtScanFolders.size()) || (ctScanDicomFolders.size() != setCtScanDicomFolders.size())) {
            fileUtils.writeBeansToFile(ctScanList, failedValidationFileOutput);
            logger.error("Scan folders not unique, exiting application.");
            System.exit(1);

        }

    }

    public void validateStandardizedFolderNames(List<CtScan> ctScanList, String failedValidationFileOutput) {
        int validScans;
        validScans = validateStandardizedFolderNames(ctScanList);
        if (validScans != ctScanList.size()) {
            logger.error("Not all scans passed validation of new standardized folder, migration will not proceed. " +
                    "Please see the" +
                    "file " + failedValidationFileOutput + " for further details.");
            fileUtils.writeBeansToFile(ctScanList, failedValidationFileOutput);
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
            if (currentScanValid) {
                validCount += 1;
            }
        }
        return validCount;
    }


    public void validateScanData(List<CtScan> scansList, String failedValidationFileOutput) {
        int validScans;
        validScans = validateScanData(scansList); // validates if scans can be migrated, all
        // info is correct.
        if (validScans != scansList.size()) {
            logger.error("Not all scans passed validation of input data, migration will not proceed. Please see the" +
                    " file " + failedValidationFileOutput + " for further details.");
            fileUtils.writeBeansToFile(scansList, failedValidationFileOutput);
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
            if (currentScanValid) {
                validCount += 1;
            }
        }
        return validCount;
    }
}
