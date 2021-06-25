package com.arilab.utils;

import com.arilab.domain.CTScan;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class CTScanMigrator {

    SettingsReader settingsReader = new SettingsReader();


    public void migrateScans(List scanList) {
        Iterator<CTScan> ctScanIterator = scanList.iterator();
        while (ctScanIterator.hasNext()) {
            CTScan ctScan = ctScanIterator.next();
            migrateScan(ctScan);
        }
    }

    public void migrateScan(CTScan ctScan) {

        // Set new folder names for location folder and dicom folder
        Path newFolder = Paths.get(settingsReader.getPrependBucketString(),ctScan.getModel())
        String newFolderName = ctScan.getSpecimenCode()

        // Rename scan folder

        // Rename dicom folder

        // Move folders. If dicom folder is inside the main scan folder, move only the scan folder. Otherwise, move
        // them separately.

    }



}
