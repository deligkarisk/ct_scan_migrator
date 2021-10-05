package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class CtScanValidatorService {

    private static Logger logger = LoggerFactory.getLogger(CtScanValidatorService.class);

    CtScanValidator ctScanValidator = new CtScanValidator();
    FileUtils fileUtils = new FileUtils();
    DbUtilService dbUtilService = new DbUtilService();


    public void validateUniquenessOfFolders(List<CtScan> ctScanList, String failedValidationFileOutput) {
        // Ensure that migrated folders, and dicom folders are all unique inside the fill list of scans.
        List<String> ctScanFolders = ctScanList.stream().map(CtScan::getNewFolderPath).collect(Collectors.toList());
        ctScanFolders.removeAll(Collections.singleton(null));
        Set<String> setCtScanFolders = new HashSet<String>(ctScanFolders);

        List<String> ctScanDicomFolders = ctScanList.stream().map(CtScan::getDicomFolderLocation).collect(Collectors.toList());
        ctScanDicomFolders.removeAll(Collections.singleton(null));
        Set<String> setCtScanDicomFolders = new HashSet<String>(ctScanDicomFolders);

        List<String> ctScanOldFolders = ctScanList.stream().map(CtScan::getFolderLocation).collect(Collectors.toList());
        ctScanOldFolders.removeAll(Collections.singleton(null));
        Set<String> setCtScanOldFolders = new HashSet<String>(ctScanOldFolders);


        if ((ctScanFolders.size() != setCtScanFolders.size()) || (ctScanDicomFolders.size() != setCtScanDicomFolders.size())
                || (ctScanOldFolders.size() != setCtScanOldFolders.size())) {
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


    public void validateScanData(List ctScanList) {
        Iterator<CtScan> ctScanIterator = ctScanList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            logger.info("Validating scan " + ctScan.getSpecimenCode() + ", " + ctScan.getFolderLocation());
            ctScan.setSpecimenCodeExists(dbUtilService.specimenCodeExists(ctScan.getSpecimenCode()));
            ctScan.setWetDryCombinationIsCorrect(ctScanValidator.wetDryCombinationIsCorrect(ctScan));
            ctScan.setDryMethodIsCorrect(ctScanValidator.dryMethodCheck(ctScan));
            ctScan.setBodypartIsCorrect(ctScanValidator.bodypartCheck(ctScan));
            ctScan.setFolderLocationExists(ctScanValidator.folderLocationExists(ctScan));
            ctScan.setModelIsCorrect(ctScanValidator.modelIsAnts(ctScan));
            ctScan.setStainingIsCorrect(ctScanValidator.stainingIsCorrect(ctScan));
            ctScan.setEthanolConcIsCorrect(ctScanValidator.ethanolConcIsCorrect(ctScan));
            ctScan.setAntscanCodingIsCorrect(ctScanValidator.antscanIsCorrect(ctScan));
            ctScan.setDicomFolderNotAChildOfMain(ctScanValidator.dicomFolderNotInMainFolder(ctScan));
            ctScan.setAllinputDataIsValid(ctScanValidator.allInputDataValidationsPassed(ctScan));
        }
    }
}