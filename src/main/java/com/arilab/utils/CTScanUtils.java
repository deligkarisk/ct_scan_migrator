package com.arilab.utils;

import com.arilab.domain.CTScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CTScanUtils {

    private static Logger logger = LoggerFactory.getLogger(CTScanUtils.class);
    private static SettingsReader settingsReader = new SettingsReader();
    private static DBTool dbTool = new DBTool();
    private static CTScanUtils ctScanUtils = new CTScanUtils();


    public void findNewFolderName(CTScan ctScan) {
        String genus = ctScanUtils.findGenus(ctScan);
        String speciesMorphoCode = ctScanUtils.findSpeciesNameOrMorphoCode(ctScan);
        String uniqueFolderID;
        if (ctScan.getSpecialIdentifier() == null) {
            uniqueFolderID =
                    ctScan.getSpecimenCode() + "_" + genus.substring(0,
                                                                     3) + "_" + ctScan.getBodyPart() + "_" + ctScan.getTimestamp();

        } else {
            uniqueFolderID =
                    ctScan.getSpecimenCode() + "_" + genus.substring(0,
                                                                     3) + "_" + ctScan.getBodyPart() + "_" + ctScan.getSpecialIdentifier() + "_" + ctScan.getTimestamp();
        }
        Path newFolder = Paths.get(settingsReader.getPrependBucketStringNew(), ctScan.getModel(), genus,
                                   speciesMorphoCode, uniqueFolderID);
        ctScan.setNewFolderPath(newFolder.toString());
    }

    public void findScanCreatedDate(CTScan ctScan) {
        Path scanPath = Paths.get(ctScan.getFolderLocation());
        try {
            BasicFileAttributes attributes = Files.readAttributes(scanPath, BasicFileAttributes.class);
            ctScan.setFolderCreationTime(attributes.creationTime().toString());
        } catch (IOException exception) {
            logger.error("Exception during reading file attributes " + exception);
            exception.printStackTrace();
        }
    }

    public String findTimestampFromFolderName(CTScan ctScan) {
        String returnString = null;
        Pattern pattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d_\\d\\d\\d\\d\\d\\d");
        Matcher matcher = pattern.matcher(ctScan.getFolderLocation());
        if (matcher.find()) {
            returnString = matcher.group(0);
        }
        return returnString;
    }

    public String createTimestampFromScanDate(CTScan ctScan) {
        String timestampFromScanDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            timestampFromScanDate = sdf.format(sdf.parse(ctScan.getScanDate()));
        } catch (ParseException exception) {
            logger.error("Could not parse scan date, exiting...");
            System.exit(1);
        }
        return timestampFromScanDate;
    }

    public void setTimestampOnCTScan(CTScan ctScan) {
        String timestamp = null;
        String tsFromFolderName = findTimestampFromFolderName(ctScan);
        if (tsFromFolderName != null) {
            timestamp = tsFromFolderName;
        } else {
            timestamp = createTimestampFromScanDate(ctScan);
        }

        ctScan.setTimestamp(timestamp);
    }

    public String findGenus(CTScan ctScan) {
        return dbTool.getGenusFromSpecimenCode(ctScan.getSpecimenCode());
    }

    public String findSpeciesNameOrMorphoCode(CTScan ctScan) {
        String speciesOrMorphoCode = null;
        String speciesName = dbTool.getSpeciesNameFromSpecimenCode(ctScan.getSpecimenCode());
        String morphoCode = dbTool.getMorphoCodeFromSpecimenCode(ctScan.getSpecimenCode());

        if ((speciesName == null) && (morphoCode == null)) {
            logger.error("No species name or morpho code found for this specimen code." + ctScan.getSpecimenCode());
            System.exit(1);
        }

        if (speciesName != null) {
            speciesOrMorphoCode = speciesName;
        } else {
            speciesOrMorphoCode = morphoCode;
        }

        return speciesOrMorphoCode;
    }

    public void fixScanFolderLocations(CTScan ctScan) {
        String currentLocation = ctScan.getFolderLocation();
        String newLocation = currentLocation.split("CT_Scan_2017-2019")[1];
        newLocation = Paths.get(settingsReader.getPrependBucketStringOld(), newLocation).toString();
        logger.info("Replacing old folder: \n" + currentLocation + " with \n" + newLocation);
        ctScan.setFolderLocation(newLocation);
    }

    public CTScan findDicomFolderLocation(CTScan ctScan, int levelsBack, String appendString) {
        String folderLocation = ctScan.getFolderLocation();
        Path dicomFolderPath = Paths.get(folderLocation);
        for (int i = 0; i < levelsBack; i++) {
            dicomFolderPath = dicomFolderPath.getParent();
        }
        String dicomStringPath = Paths.get(dicomFolderPath.getParent().toString(),
                                           dicomFolderPath.getFileName().toString() + appendString).toString();

        //dicomStringPath = dicomStringPath.split("CT_Scan_2017-2019")[1];


        logger.info("Setting up dicom folder: " + dicomStringPath);
        ctScan.setDicomFolderLocation(dicomStringPath);

        return ctScan;
    }

}
