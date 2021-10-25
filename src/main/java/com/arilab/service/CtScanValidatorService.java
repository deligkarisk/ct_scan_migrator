package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CtScanValidatorService {

    private static Logger logger = LoggerFactory.getLogger(CtScanValidatorService.class);

    CtScanValidator ctScanValidator;
    FileUtils fileUtils;
    DatabaseService databaseService;


    public CtScanValidatorService(CtScanValidator ctScanValidator, FileUtils fileUtils, DatabaseService databaseService) {
        this.ctScanValidator = ctScanValidator;
        this.fileUtils = fileUtils;
        this.databaseService = databaseService;
    }

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






}