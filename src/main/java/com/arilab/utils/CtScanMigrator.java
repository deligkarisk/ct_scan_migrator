package com.arilab.utils;

import com.arilab.domain.CtScan;

import java.util.Iterator;
import java.util.List;

public class CtScanMigrator {

    SettingsReader settingsReader = new SettingsReader();
    CtScanUtils ctScanUtils = new CtScanUtils();


    public void migrateScans(List scanList) {
        Iterator<CtScan> ctScanIterator = scanList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            migrateScan(ctScan);
        }
    }

    public void migrateScan(CtScan ctScan) {

        // Set new folder names for location folder and dicom folder


        // Rename scan folder

        // Rename dicom folder

        // Move folders. If dicom folder is inside the main scan folder, move only the scan folder. Otherwise, move
        // them separately.

    }



}
