package com.arilab.domain;

import com.arilab.service.DbService;
import com.arilab.utils.DbUtil;
import com.arilab.utils.PathUtils;
import com.arilab.utils.SettingsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CtScanValidator {

    SettingsReader settingsReader = new SettingsReader();

    private static Logger logger = LoggerFactory.getLogger(CtScanValidator.class);
    DbUtil dbUtil = new DbUtil();
    DbService dbService = new DbService();
    PathUtils pathUtils = new PathUtils();


    public Boolean validateInputData(CtScan ctScan) {
        ctScan.setSpecimenCodeExists(dbService.specimenCodeExists(ctScan.getSpecimenCode()));
        ctScan.setWetDryCombinationIsCorrect(wetDryCombinationIsCorrect(ctScan));
        ctScan.setDryMethodIsCorrect(dryMethodCheck(ctScan));
        ctScan.setBodypartIsCorrect(bodypartCheck(ctScan));
        ctScan.setInputDataIsValid(allInputDataValidationsPassed(ctScan));
        return ctScan.getInputDataIsValid();
    }


    public Boolean wetDryCombinationIsCorrect(CtScan ctScan) {
        if (ctScan.getWet().equals(
                "Yes") && ctScan.getDryMethod() == null && ctScan.getEthanolConcentration() != null) {
            return true;
        }
        if (ctScan.getWet().equals("No") && ctScan.getDryMethod() != null && ctScan.getEthanolConcentration() == null) {
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
                ctScan.getDryMethodIsCorrect() && ctScan.getBodypartIsCorrect()) {
            return true;
        }
        return false;
    }

    public Boolean validateStandardizedFolder(CtScan ctScan) {
        // New folders should not exist already
        return !pathUtils.folderExists(Paths.get(ctScan.getNewFolderPath()));
    }

}


