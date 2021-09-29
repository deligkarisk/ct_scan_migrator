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
import java.util.Locale;

public class CtScanUtils {

    private static Logger logger = LoggerFactory.getLogger(CtScanUtils.class);
    private static DbUtil dbUtil = new DbUtil();
    private static DbUtilService dbUtilService = new DbUtilService();
    private static CtScanUtils ctScanUtils = new CtScanUtils();
    private static PathUtils pathUtils = new PathUtils();
    private Config config = Config.getInstance();


    public String findDicomFolderLocation(CtScan ctScan) {

            String folderLocation = ctScan.getFolderLocation();
            String foundLocation = null;

            // Try to find the dicom folder based on settings.
            String dicomFolderLocation = pathUtils.generateDicomPotentialFolder(folderLocation, config.dicomLevelsUp, config.dicomAppendString);
            if (Files.exists(Paths.get(dicomFolderLocation))) {
                foundLocation = dicomFolderLocation;

            } else {
                // retry with all characters of appendString to lower case.
                dicomFolderLocation = pathUtils.generateDicomPotentialFolder(folderLocation, config.dicomLevelsUp, config.dicomAppendString.toLowerCase(Locale.ROOT));
                if (Files.exists(Paths.get(dicomFolderLocation))) {
                    foundLocation = dicomFolderLocation;
                }
            }
            return foundLocation;
    }



    public String createTimestampFromScanDate(CtScan ctScan) {
        String timestamp = null;
        try {
            SimpleDateFormat sdf = convertToSimpleDateFormat("yyyy-mm-dd");
            timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
        } catch (ParseException exception) {
            try {
                SimpleDateFormat sdf = convertToSimpleDateFormat("yyyy-mm");
                timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
            } catch (ParseException e) {
                try {
                    SimpleDateFormat sdf = convertToSimpleDateFormat("yyyy");
                    timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
                } catch (ParseException ignored) {
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
        Path newFolder = Paths.get(config.targetDirectory, ctScan.getModel(), genus,
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



}
