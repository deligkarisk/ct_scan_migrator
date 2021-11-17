package com.arilab.domain;

import com.arilab.service.DatabaseService;
import com.arilab.utils.FileUtils;
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
    FileUtils fileUtils;

    public CtScanValidator(DatabaseService databaseService, FileUtils fileUtils) {
        this.databaseService = databaseService;
        this.fileUtils= fileUtils;
    }




    public Boolean dicomFolderNotInMainFolder(CtScan ctScan) {
        Boolean returnValue = true;

        if (ctScan.getDicomFolderLocation() != null) {
            Path dicomPath = Paths.get(ctScan.getDicomFolderLocation());
            Path mainDataPath = Paths.get(ctScan.getFolderLocation());


            returnValue = !dicomPath.startsWith(mainDataPath);

        }

        return returnValue;

    }


    public Boolean folderLocationExists(CtScan ctScan) {
        Path location = Paths.get(ctScan.getFolderLocation());
        return Files.exists(location);
    }

    public Boolean dicomFolderLocationExists(CtScan ctScan) {

        if (ctScan.getDicomFolderLocation() == null) {
            return true;
        }

        Path dicomPath = Paths.get(ctScan.getDicomFolderLocation());
        return Files.exists(dicomPath);
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


    public boolean specimenCodeExists(String specimenCode) throws SQLException {
        return databaseService.specimenCodeExists(specimenCode);
    }

    public boolean standardizedFolderIsAvailable(CtScan ctScan) {
        return !fileUtils.folderExists(Paths.get(ctScan.getNewFolderPath()));
    }

}
