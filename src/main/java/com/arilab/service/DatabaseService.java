package com.arilab.service;

import com.arilab.repository.DatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DatabaseService {

    DatabaseRepository databaseRepository;

    private static Logger logger = LoggerFactory.getLogger(DatabaseService.class);


    public DatabaseService(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public Boolean specimenCodeExists(String specimenCode) throws SQLException {
        return databaseRepository.specimenCodeExists(specimenCode);
    }


    public String findSpeciesNameOrMorphoCodeFromSpecimenCode(String specimenCode) {
        // todo: replace the system exit function with an exception
        String speciesOrMorphoCode = null;
        String speciesName = databaseRepository.getSpeciesNameFromSpecimenCode(specimenCode);
        String morphoCode = databaseRepository.getMorphoCodeFromSpecimenCode(specimenCode);

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
        return databaseRepository.getGenusFromSpecimenCode(specimenCode);
    }

    public void databaseConnectivityCheck() throws SQLException {
            specimenCodeExists("Test");
    }
}
