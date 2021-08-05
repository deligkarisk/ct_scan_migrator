package com.arilab.utils;

import com.arilab.domain.CtScan;
import com.arilab.service.DbUtilService;
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
    private static DbUtilService dbUtilService = new DbUtilService();
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
        logger.info("Replacing old folder location with " + newLocation);
        ctScan.setFolderLocation(newLocation.toString());
    }

    public String findTimestampFromFolderName(CtScan ctScan) {
        String timestamp = pathUtils.extractTimestamp(ctScan.getFolderLocation());
        if (timestamp != null) {
            ctScan.setScanDateCorrect(true);
        }
        return timestamp;
    }

    public String createTimestampFromScanDate(CtScan ctScan) {
        String timestamp = null;
        try {
            SimpleDateFormat sdf = convertToSimpleDateFormat("yyyy-mm-dd");
            timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
            ctScan.setScanDateCorrect(true);
        } catch (ParseException exception) {
            try {
                SimpleDateFormat sdf = convertToSimpleDateFormat("yyyy-mm");
                timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
                ctScan.setScanDateCorrect(true);
            } catch (ParseException e) {
                try {
                    SimpleDateFormat sdf = convertToSimpleDateFormat("yyyy");
                    timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
                    ctScan.setScanDateCorrect(true);
                } catch (ParseException parseException) {
                    ctScan.setScanDateCorrect(false);
                }
            }
        }
        return timestamp;
    }


    private SimpleDateFormat convertToSimpleDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        return sdf;
    }


    public void findStandardizedFolderName(CtScan ctScan) {
        String genus = dbUtilService.findGenusFromSpecimenCode(ctScan.getSpecimenCode());
        String speciesMorphoCode = dbUtilService.findSpeciesNameOrMorphoCodeFromSpecimenCode(ctScan.getSpecimenCode());
        String uniqueFolderID = createUniqueFolderId(ctScan, genus);
        Path newFolder = Paths.get(settingsReader.getPrependBucketStringNew(), ctScan.getModel(), genus,
                speciesMorphoCode, uniqueFolderID);
        ctScan.setNewFolderPath(newFolder.toString());
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
