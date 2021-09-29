package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.utils.CtScanUtils;
import com.arilab.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class CTScanService {

    private static final Logger logger = LoggerFactory.getLogger(CTScanService.class);


    PathUtils pathUtils = new PathUtils();
    CtScanUtils ctScanUtils = new CtScanUtils();

    public void preprocessScanFolderLocation(CtScan ctScan) {
        Path newLocation = pathUtils.getCorrectScanFolderLocation(ctScan.getFolderLocation());
        logger.info("Replacing old folder location with " + newLocation);
        ctScan.setFolderLocation(newLocation.toString());

        if (ctScan.getDicomFolderLocation() != null) {
            Path newDicomFolder = pathUtils.getCorrectScanFolderLocation(ctScan.getDicomFolderLocation());
            logger.info("Replacing old dicom folder location with " + newDicomFolder);
            ctScan.setDicomFolderLocation(newDicomFolder.toString());
        }
    }

    public void updateDicomFolder(CtScan ctScan) {
        if (ctScan.getDicomFolderLocation() != null) {
            logger.info("Dicom folder already inputted, nothing to add: " + ctScan.getDicomFolderLocation());
            return;
        }

        logger.info("Attempting to automatically find the dicom folder.");
        String newDicomFolder = ctScanUtils.findDicomFolderLocation(ctScan);
        if (newDicomFolder != null) {
            ctScan.setDicomFolderLocation(newDicomFolder);
            logger.info("Dicom folder found, using this: " + newDicomFolder);
        } else {
            logger.info("Dicom folder not found, leaving it as null");
        }
    }

    public void updateTimestamp(CtScan ctScan) {

        String timestamp;

        timestamp = pathUtils.extractTimestamp(ctScan.getFolderLocation());

        if (timestamp == null) {
            timestamp = ctScanUtils.createTimestampFromScanDate(ctScan);
        }

        if (timestamp == null) {
            timestamp = "Unknown_date";
        }

        ctScan.setTimestamp(timestamp);

    }
}
