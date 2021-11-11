package com.arilab.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class CtScanCollectionValidator {

    private static Logger logger = LoggerFactory.getLogger(CtScanCollectionValidator.class);


    public boolean areAllFoldersUniqueInCollection(CtScanCollection ctScanCollection) {
        List<CtScan> ctScanList = ctScanCollection.getCtScans();
        // Ensure that migrated folders, and dicom folders are all unique inside the fill list of scans.
        List<String> ctScanFolders = ctScanList.stream().map(CtScan::getNewFolderPath).collect(Collectors.toList());
        List<String> ctScanDicomFolders = ctScanList.stream().map(CtScan::getDicomFolderLocation).collect(Collectors.toList());
        List<String> ctScanOldFolders = ctScanList.stream().map(CtScan::getFolderLocation).collect(Collectors.toList());

        return areAllFoldersUnique(ctScanFolders) && areAllFoldersUnique(ctScanDicomFolders) && areAllFoldersUnique(ctScanOldFolders);

    }

    private boolean areAllFoldersUnique(List<String> folderList) {
        // first remove all nulls
        folderList.removeAll(Collections.singleton(null));

        Set<String> foldersSet = new HashSet<String>(folderList);
        return (foldersSet.size() == folderList.size());
    }


}