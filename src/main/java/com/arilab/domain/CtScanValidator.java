package com.arilab.domain;

import com.arilab.service.DatabaseService;
import com.arilab.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CtScanValidator {

    private static Logger logger = LoggerFactory.getLogger(CtScanValidator.class);
    DatabaseService databaseService;

    public CtScanValidator(DatabaseService databaseService, PathUtils pathUtils) {
        this.databaseService = databaseService;
        this.pathUtils = pathUtils;
    }

    PathUtils pathUtils;


    public Boolean dicomFolderNotInMainFolder(CtScan ctScan) {
        Boolean returnValue = true;

        if (ctScan.getDicomFolderLocation() != null) {
            Path dicomPath = Paths.get(ctScan.getDicomFolderLocation());
            Path mainDataPath = Paths.get(ctScan.getFolderLocation());

            if (!Files.exists(dicomPath)) {
                logger.error("Dicom path does not exist, exiting... :" + dicomPath.toString());
                System.exit(1);
            }

            returnValue = !dicomPath.startsWith(mainDataPath);

        }

        return returnValue;

    }

    public Boolean antscanIsCorrect(CtScan ctScan) {
        Boolean isCorrect = ((ctScan.getAntscan().equals("No") && ctScan.getAntscanCode() == null) || (ctScan.getAntscan().equals("Yes") && ctScan.getAntscanCode() != null));
        return isCorrect;
    }

    public Boolean stainingIsCorrect(CtScan ctScan) {
        List<String> stainingValues = Arrays.asList("Iodine", "PTA", "Osmium", "No staining");
        return stainingValues.contains(ctScan.getStaining());
    }

    public Boolean ethanolConcIsCorrect(CtScan ctScan) {
        List<String> ethanolConcValues = Arrays.asList("No ethanol used", "70%", "95%", "99%", "Not known");
        return ethanolConcValues.contains(ctScan.getEthanolConcentration());
    }

    public Boolean modelIsAnts(CtScan ctScan) {
        return ctScan.getModel().equals("Ants");
    }

    public Boolean wetDryCombinationIsCorrect(CtScan ctScan) {
        if (ctScan.getWet().equals(
                "Yes") && ctScan.getDryMethod() == null && ctScan.getEthanolConcentration() != null) {
            return true;
        }
        if (ctScan.getWet().equals("No") && ctScan.getDryMethod() != null && ctScan.getEthanolConcentration().equals("No ethanol used")) {
            return true;
        }
        return false;
    }

    public Boolean dryMethodCheck(CtScan ctScan) {
        if (ctScan.getDryMethod() == null) {
            return true;
        }
        if (ctScan.getDryMethod().equals("Pin") || ctScan.getDryMethod().equals(
                "Amber") || ctScan.getDryMethod().equals("Resin") || ctScan.getDryMethod().equals("CPD")
                || ctScan.getDryMethod().equals("Freeze_dried")) {
            return true;
        }
        return false;
    }

    public Boolean bodypartCheck(CtScan ctScan) {
        List<String> validValues = Arrays.asList("Head", "Whole_body", "Legs", "Thorax",
                                                 "Abdomen");
        if (ctScan.getBodyPart() == null) {
            return false;
        }
        if (validValues.contains(ctScan.getBodyPart())) {
            return true;
        }
        return false;
    }



    public Boolean validateStandardizedFolder(CtScan ctScan) {
        // New folders should not exist already
        Boolean folderIsAvailable = !pathUtils.folderExists(Paths.get(ctScan.getNewFolderPath()));
        ctScan.setNewFolderPathAvailable(folderIsAvailable);
        Boolean noDatabaseEntryWithSameFolderExists = !databaseService.ctScanFolderExists(ctScan.getNewFolderPath());
        ctScan.setNewFolderPathAvailableIntheDatabase(noDatabaseEntryWithSameFolderExists);
        return (folderIsAvailable && noDatabaseEntryWithSameFolderExists);
    }

    public Boolean folderLocationExists(CtScan ctScan) {
        Path location = Paths.get(ctScan.getFolderLocation());
        return Files.exists(location);
    }


    public boolean specimenCodeExists(String specimenCode) throws SQLException {
        return databaseService.specimenCodeExists(specimenCode);

    }



}


