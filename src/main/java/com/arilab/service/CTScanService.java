package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.repository.CtScanRepository;
import com.arilab.utils.Config;
import com.arilab.utils.CtScanUtils;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CTScanService {

    private static final Logger logger = LoggerFactory.getLogger(CTScanService.class);


    FileUtils fileUtils;
    CtScanUtils ctScanUtils;
    CtScanRepository ctScanRepository;
    CtScanValidator ctScanValidator;
    DatabaseService databaseService;
    Config config;

    public CTScanService(FileUtils fileUtils, CtScanUtils ctScanUtils, CtScanRepository ctScanRepository,
                         CtScanValidator ctScanValidator, DatabaseService databaseService, Config config) {
        this.fileUtils = fileUtils;
        this.ctScanUtils = ctScanUtils;
        this.ctScanRepository = ctScanRepository;
        this.ctScanValidator = ctScanValidator;
        this.databaseService = databaseService;
        this.config = config;
    }



    private boolean allInputDataValidationsPassed(CtScan ctScan) {
        if (ctScan.getSpecimenCodeExists() &&
                ctScan.getWetDryCombinationIsCorrect() &&
                ctScan.getDryMethodIsCorrect() &&
                ctScan.getBodypartIsCorrect() &&
                ctScan.getFolderLocationExists() &&
                ctScan.getModelIsCorrect() &&
                ctScan.getEthanolConcIsCorrect() &&
                ctScan.getStainingIsCorrect() &&
                ctScan.getAntscanCodingIsCorrect() &&
                ctScan.getDicomFolderLocationExists() &&
                ctScan.getDicomFolderNotAChildOfMain()) {
            return true;
        }
        return false;
    }


    public void preprocessScanFolderLocation(CtScan ctScan) {
        Path newLocation = findCorrectPath(ctScan.getFolderLocation());
        logger.info("Replacing old folder location with " + newLocation);
        ctScan.setFolderLocation(newLocation.toString());

        if (ctScan.getDicomFolderLocation() != null) {
            Path newDicomFolder = findCorrectPath(ctScan.getDicomFolderLocation());
            logger.info("Replacing old dicom folder location with " + newDicomFolder);
            ctScan.setDicomFolderLocation(newDicomFolder.toString());
        }
    }

    private Path findCorrectPath(String currentLocation) {
        String parentFolderToSplit = Paths.get(config.getSourceDirectory()).getFileName().toString();
        boolean canSplit = currentLocation.contains(parentFolderToSplit);
        if (!canSplit) {
            logger.error("Can't find the parent folder " + parentFolderToSplit + " in " + currentLocation + ". " +
                    "Aborting" +
                    " operations.");
            throw new RuntimeException("Unable to fix the folder location. Aborting.");
        }

        String[] splitResult = currentLocation.split(parentFolderToSplit);
        String newLocation = splitResult[1];
        return Paths.get(config.getSourceDirectory(), newLocation);
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

        timestamp = extractTimestamp(ctScan.getFolderLocation());

        if (timestamp == null) {
            timestamp = ctScanUtils.createTimestampFromScanDate(ctScan);
        }

        if (timestamp == null) {
            timestamp = "Unknown_date";
        }

        ctScan.setTimestamp(timestamp);
    }

    private String extractTimestamp(String folderPath) {
        String returnString = null;
        Pattern pattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d_\\d\\d\\d\\d\\d\\d");
        Matcher matcher = pattern.matcher(folderPath);
        if (matcher.find()) {
            returnString = matcher.group(0);
        }
        return returnString;
    }

    public Boolean ctScanFolderExists(String folder) throws SQLException {
        Boolean ctScanFolderExists = ctScanRepository.findCtScanFolder(folder).isBeforeFirst();
        return ctScanFolderExists;
    }


    public void setNewFolderPaths(CtScan ctScan) {
        String newFolderName = findNewMainFolderPath(ctScan);
        ctScan.setNewFolderPath(newFolderName);

        if (ctScan.getDicomFolderLocation() != null) {
            String newDicomFolderName = findNewDicomFolderPath(newFolderName);
            ctScan.setNewDicomFolderPath(newDicomFolderName);
        }
    }

    private String findNewMainFolderPath(CtScan ctScan) {
        String genus = databaseService.findGenusFromSpecimenCode(ctScan.getSpecimenCode());
        String speciesMorphoCode = databaseService.findSpeciesNameOrMorphoCodeFromSpecimenCode(ctScan.getSpecimenCode());
        String uniqueFolderID = createUniqueFolderId(ctScan, genus);
        Path newFolder = Paths.get(config.getTargetDirectory(), ctScan.getModel(), genus,
                speciesMorphoCode, uniqueFolderID);
        return newFolder.toString();
    }

    private String findNewDicomFolderPath(String newFolderName) {
        String label = Paths.get(newFolderName).getFileName().toString();
        String dicomFileLabel = label + "_dicom";
        File dicomDestinationDir = new File(newFolderName, dicomFileLabel);

        if (fileUtils.folderExists(dicomDestinationDir.toPath()))  {
            dicomFileLabel = label + "_dicom2";
            dicomDestinationDir = new File(newFolderName, dicomFileLabel);
        }

        return dicomDestinationDir.toString();
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



    public void validateStandardizedFolder(CtScan ctScan) throws SQLException {
        // New folders should not exist already
        Boolean folderIsAvailable = ctScanValidator.standardizedFolderIsAvailable(ctScan);
        ctScan.setNewFolderPathAvailable(folderIsAvailable);

        // New folders should not exist in the db either
        Boolean noDatabaseEntryWithSameFolderExists = !ctScanFolderExists(ctScan.getNewFolderPath());
        ctScan.setNewFolderPathAvailableIntheDatabase(noDatabaseEntryWithSameFolderExists);
    }
}

