package com.arilab.service;

import com.arilab.utils.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUtilService {

    DbUtil dbUtil = new DbUtil();
    private static Logger logger = LoggerFactory.getLogger(DbUtilService.class);


    public Boolean specimenCodeExists(String specimenCode) {
        Boolean specimenCodeExists = dbUtil.specimenCodeExists(specimenCode);
        if (!specimenCodeExists) {
            logger.error("Specimen code does not exist: " + specimenCode);
        }
        return specimenCodeExists;
    }

    public Boolean ctScanFolderExists(String folder) {
        Boolean ctScanFolderExists = dbUtil.ctScanFolderExists(folder);
        return ctScanFolderExists;
    }


    public String findSpeciesNameOrMorphoCodeFromSpecimenCode(String specimenCode) {
        String speciesOrMorphoCode = null;
        String speciesName = dbUtil.getSpeciesNameFromSpecimenCode(specimenCode);
        String morphoCode = dbUtil.getMorphoCodeFromSpecimenCode(specimenCode);

        if ((speciesName == null) && (morphoCode == null)) {
            logger.error("No species name or morpho code found for this specimen code." + specimenCode);
            System.exit(1);
        }

        if (speciesName != null) {
            speciesOrMorphoCode = speciesName;
        } else {
            speciesOrMorphoCode = morphoCode;
        }

        return speciesOrMorphoCode;
    }



    public String findGenusFromSpecimenCode(String specimenCode) {
        return dbUtil.getGenusFromSpecimenCode(specimenCode);
    }


}
