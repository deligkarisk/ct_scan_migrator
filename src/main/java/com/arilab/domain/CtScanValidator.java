package com.arilab.domain;

import com.arilab.service.DbUtilService;
import com.arilab.utils.DbUtil;
import com.arilab.utils.PathUtils;
import com.arilab.utils.SettingsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CtScanValidator {

    SettingsReader settingsReader = new SettingsReader();

    private static Logger logger = LoggerFactory.getLogger(CtScanValidator.class);
    DbUtil dbUtil = new DbUtil();
    DbUtilService dbUtilService = new DbUtilService();
    PathUtils pathUtils = new PathUtils();


    public Boolean validateInputData(CtScan ctScan) {
        ctScan.setSpecimenCodeExists(dbUtilService.specimenCodeExists(ctScan.getSpecimenCode()));
        ctScan.setWetDryCombinationIsCorrect(wetDryCombinationIsCorrect(ctScan));
        ctScan.setDryMethodIsCorrect(dryMethodCheck(ctScan));
        ctScan.setBodypartIsCorrect(bodypartCheck(ctScan));
        ctScan.setFolderLocationExists(folderLocationExists(ctScan));
        ctScan.setModelIsCorrect(modelIsAnts(ctScan));
        ctScan.setStainingIsCorrect(stainingIsCorrect(ctScan));
        ctScan.setEthanolConcIsCorrect(ethanolConcIsCorrect(ctScan));
        ctScan.setAntscanCodingIsCorrect(antscanIsCorrect(ctScan));
        ctScan.setAllinputDataIsValid(allInputDataValidationsPassed(ctScan));
        return ctScan.getAllinputDataIsValid();
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

    public Boolean allInputDataValidationsPassed(CtScan ctScan) {
        if (ctScan.getSpecimenCodeExists() && ctScan.getWetDryCombinationIsCorrect() &&
                ctScan.getDryMethodIsCorrect() && ctScan.getBodypartIsCorrect() && ctScan.getFolderLocationExists()
        & ctScan.getModelIsCorrect() && ctScan.getEthanolConcIsCorrect() && ctScan.getStainingIsCorrect()
        && ctScan.getAntscanCodingIsCorrect() && ctScan.getScanDateCorrect()) {
            return true;
        }
        return false;
    }

    public Boolean validateStandardizedFolder(CtScan ctScan) {
        // New folders should not exist already
        Boolean folderIsAvailable = !pathUtils.folderExists(Paths.get(ctScan.getNewFolderPath()));
        ctScan.setNewFolderPathAvailable(folderIsAvailable);
        Boolean noDatabaseEntryWithSameFolderExists = !dbUtilService.ctScanFolderExists(ctScan.getNewFolderPath());
        ctScan.setNewFolderPathAvailableIntheDatabase(noDatabaseEntryWithSameFolderExists);
        return (folderIsAvailable && noDatabaseEntryWithSameFolderExists);
    }

    private Boolean folderLocationExists(CtScan ctScan) {
        Path location = Paths.get(ctScan.getFolderLocation());
        return Files.exists(location);
    }





}


