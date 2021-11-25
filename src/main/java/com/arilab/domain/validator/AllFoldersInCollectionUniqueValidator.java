package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;

import java.util.*;
import java.util.stream.Collectors;

public class AllFoldersInCollectionUniqueValidator implements CtScanCollectionValidator {


    @Override
    public Optional<String> validate(CtScanCollection ctScanCollection) {
        List<CtScan> ctScanList = ctScanCollection.getCtScans();
        // Ensure that migrated folders, and dicom folders are all unique inside the fill list of scans.
        List<String> ctScanFolders = ctScanList.stream().map(CtScan::getNewFolderPath).collect(Collectors.toList());
        List<String> ctScanDicomFolders = ctScanList.stream().map(CtScan::getDicomFolderLocation).collect(Collectors.toList());
        List<String> ctScanOldFolders = ctScanList.stream().map(CtScan::getFolderLocation).collect(Collectors.toList());

        if (areAllFoldersUnique(ctScanFolders) && areAllFoldersUnique(ctScanDicomFolders) && areAllFoldersUnique(ctScanOldFolders)) {
            return Optional.empty();
        }

        return Optional.of("Not all folders in the dataset are unique.");
    }

    private boolean areAllFoldersUnique(List<String> folderList) {
        // first remove all nulls
        folderList.removeAll(Collections.singleton(null));

        Set<String> foldersSet = new HashSet<String>(folderList);
        return (foldersSet.size() == folderList.size());
    }
}
