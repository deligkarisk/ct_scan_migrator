package com.arilab.utils;

import com.arilab.domain.CtScan;
import com.arilab.service.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CtScanUtils {

    private static Logger logger = LoggerFactory.getLogger(CtScanUtils.class);
    private static SettingsReader settingsReader = new SettingsReader();
    private static DbUtil dbUtil = new DbUtil();
    private static DbService dbService = new DbService();
    private static CtScanUtils ctScanUtils = new CtScanUtils();
    private static PathUtils pathUtils = new PathUtils();


    public void findDicomFolderLocation(CtScan ctScan, int levelsBack, String appendString) {
        logger.info("Attempting to automatically find the dicom folder.");
        String folderLocation = ctScan.getFolderLocation();
        String dicomFolderLocation = pathUtils.findDicomFolderLocation(folderLocation, levelsBack, appendString);
        if (Files.exists(Paths.get(dicomFolderLocation))) {
            ctScan.setDicomFolderLocation(dicomFolderLocation);
            logger.info("Dicom folder found, using this: " + dicomFolderLocation);
        } else {
            logger.info("Dicom folder not found, using null instead of dicom folder location: " + dicomFolderLocation);
        }
    }

    public void updateScanFolderLocation(CtScan ctScan) {
        Path newLocation = pathUtils.getCorrectScanFolderLocation(ctScan.getFolderLocation());
        if (!Files.exists(newLocation)) {
            logger.error("Folder lcoation does not exist: " + newLocation);
            System.exit(1);
        }
        logger.info("Replacing old folder location with " + newLocation);
        ctScan.setFolderLocation(newLocation.toString());
    }

    public String findTimestampFromFolderName(CtScan ctScan) {
        return pathUtils.extractTimestamp(ctScan.getFolderLocation());
    }

    public String createTimestampFromScanDate(CtScan ctScan) {
        String timestamp = null;
        try {
            SimpleDateFormat sdf = convertToSimpleDateFormat(ctScan.getScanDate());
            timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
        } catch (ParseException exception) {
            logger.error("Could not parse scan date, exiting...");
            System.exit(1);
        }
        return timestamp;
    }



    private SimpleDateFormat convertToSimpleDateFormat(String scanDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return sdf;
    }


    public void findStandardizedFolderName(CtScan ctScan) {
        String genus = dbService.findGenusFromSpecimenCode(ctScan.getSpecimenCode());
        String speciesMorphoCode = dbService.findSpeciesNameOrMorphoCodeFromSpecimenCode(ctScan.getSpecimenCode());
        String uniqueFolderID = createUniqueFolderId(ctScan, genus);
        Path newFolder = Paths.get(settingsReader.getPrependBucketStringNew(), ctScan.getModel(), genus,
                                   speciesMorphoCode, uniqueFolderID);
        logger.info("Checking standardized folder: \n" + newFolder + "\n for ct scan: \n " + ctScan.getFolderLocation());
        if (Files.exists(newFolder)) {
            logger.error("standardized folder already exists, aborting operation.");
        } else {
            logger.info("standardized folder available, OK");
            ctScan.setNewFolderPath(newFolder.toString());
            ctScan.setNewFolderPathAvailable(true);
        }
    }

    private String createUniqueFolderId(CtScan ctScan, String genus) {
        String uniqueFolderID = null;
        if (ctScan.getSpecialIdentifier() == null) {
            uniqueFolderID =
                    ctScan.getSpecimenCode() + "_" + genus.substring(0,
                                                                     3) + "_" + ctScan.getBodyPart() + "_" + ctScan.getTimestamp();

        } else {
            uniqueFolderID =
                    ctScan.getSpecimenCode() + "_" + genus.substring(0,
                                                                     3) + "_" + ctScan.getBodyPart() + "_" + ctScan.getSpecialIdentifier() + "_" + ctScan.getTimestamp();
        }

        return uniqueFolderID;
    }

    public void updateTimestamp(CtScan ctScan) {
        String timestamp = ctScanUtils.findTimestampFromFolderName(ctScan);

        if (timestamp == null) {
            timestamp = ctScanUtils.createTimestampFromScanDate(ctScan);
        }

        if (timestamp == null) {
            timestamp = "Unknown_date";
        }

        ctScan.setTimestamp(timestamp);
    }

}
