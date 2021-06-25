package com.arilab.utils;

import com.arilab.domain.CTScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;

public class CTScanUtils {

    private static Logger logger = LoggerFactory.getLogger(CTScanUtils.class);
    private static SettingsReader settingsReader = new SettingsReader();

    public void fixScans(List scanList) {
        Iterator<CTScan> ctScanIterator = scanList.iterator();
        while (ctScanIterator.hasNext()) {
            CTScan ctScan = ctScanIterator.next();
            fixScanFolderLocations(ctScan);
            findDicomFolderLocation(ctScan, 1, "_OUT");
            findScanCreatedDate(ctScan);
        }

    }

    public void findScanCreatedDate(CTScan ctScan)  {
        Path scanPath = Paths.get(settingsReader.getPrependBucketString(), ctScan.getFolderLocation());
        try {
            BasicFileAttributes attributes = Files.readAttributes(scanPath, BasicFileAttributes.class);
            ctScan.setFolderCreationTime(attributes.creationTime().toString());
        } catch (IOException exception) {
            logger.error("Exception during reading file attributes " + exception);
            exception.printStackTrace();
        }



    }

    public void fixScanFolderLocations(CTScan ctScan) {
        String currentLocation = ctScan.getFolderLocation();
        String newLocation = currentLocation.split("CT_Scan_2017-2019")[1];
        logger.info("Replacing old folder: " + currentLocation + " with " + newLocation);
        ctScan.setFolderLocation(newLocation);
    }

    public CTScan findDicomFolderLocation(CTScan ctScan, int levelsBack, String appendString) {
        String folderLocation = ctScan.getFolderLocation();
        Path dicomFolderPath = Paths.get(settingsReader.getPrependBucketString(), folderLocation);
        for (int i = 0; i < levelsBack; i++) {
            dicomFolderPath = dicomFolderPath.getParent();
        }
        String dicomStringPath = Paths.get(dicomFolderPath.getParent().toString(),
                                           dicomFolderPath.getFileName().toString() + appendString).toString();

        dicomStringPath = dicomStringPath.split("CT_Scan_2017-2019")[1];


        logger.info("Setting up dicom folder: " + dicomStringPath);
        ctScan.setDicomFolderLocation(dicomStringPath);

        return ctScan;
    }

}
