package com.arilab.domain;

import com.arilab.utils.DBTool;
import com.arilab.utils.SettingsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CTScanValidator {

    SettingsReader settingsReader = new SettingsReader();

    private static Logger logger = LoggerFactory.getLogger(CTScanValidator.class);
    DBTool dBTool = new DBTool();


    public Boolean isValid(CTScan ctScan) {
        ctScan.setSpecimenCodeExists(dBTool.specimenCodeExists(ctScan.getSpecimenCode()));
        ctScan.setScanExistsInBucket(scanExistsInBucket(ctScan));
        ctScan.setWetDryCombinationIsCorrect(wetDryCombinationIsCorrect(ctScan));
        ctScan.setDryMethodIsCorrect(dryMethodCheck(ctScan));
        ctScan.setBodypartIsCorrect(bodypartCheck(ctScan));
        ctScan.setDicomFolderExists(dicomFolderInBucket(ctScan));
        ctScan.setFolderCreationTimeExists(creationDateIsNotNull(ctScan));
        ctScan.setCanMigrate(allValidationsPassed(ctScan));
        return ctScan.getCanMigrate();
    }

    // returns the number of scans passing the validation
    public int validate(List ctScanList) {
        int validCount = 0;

        Iterator<CTScan> ctScanIterator = ctScanList.iterator();
        while (ctScanIterator.hasNext()) {
            CTScan ctScan = ctScanIterator.next();
            logger.info("Validating scan" + ctScan.getSpecimenCode() + ", " + ctScan.getFolderLocation());
            if (isValid(ctScan)) {validCount += 1;}
        }
        return validCount;
    }


    public Boolean creationDateIsNotNull(CTScan ctScan) {
        return ctScan.getFolderCreationTime() != null;
    }

    public Boolean scanExistsInBucket(CTScan ctScan) {
        Path scanPath = Paths.get(settingsReader.getPrependBucketString(), ctScan.getFolderLocation());
        return Files.exists(scanPath);
    }

    public Boolean dicomFolderInBucket(CTScan ctScan) {
        Path dicomPath = Paths.get(settingsReader.getPrependBucketString(), ctScan.getDicomFolderLocation());
        return Files.exists(dicomPath);
    }

    public Boolean wetDryCombinationIsCorrect(CTScan ctScan) {
        if (ctScan.getWet().equals(
                "Yes") && ctScan.getDryMethod() == null && ctScan.getEthanolConcentration() != null) {
            return true;
        }
        if (ctScan.getWet().equals("No") && ctScan.getDryMethod() != null && ctScan.getEthanolConcentration() == null) {
            return true;
        }
        return false;
    }

    public Boolean dryMethodCheck(CTScan ctScan) {
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

    public Boolean bodypartCheck(CTScan ctScan) {
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

    public Boolean allValidationsPassed(CTScan ctScan) {
        if (ctScan.getSpecimenCodeExists() && ctScan.getScanExistsInBucket() && ctScan.getWetDryCombinationIsCorrect() &&
                ctScan.getDryMethodIsCorrect() && ctScan.getBodypartIsCorrect() && ctScan.getDicomFolderExists()
        && ctScan.getFolderCreationTimeExists()) {
            return true;
        }
        return false;
    }


}


