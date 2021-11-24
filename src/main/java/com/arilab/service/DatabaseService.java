package com.arilab.service;

import com.arilab.repository.DatabaseRepository;
import com.arilab.system.SystemExit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DatabaseService {

    DatabaseRepository databaseRepository;
    SystemExit systemExit;

    private static Logger logger = LoggerFactory.getLogger(DatabaseService.class);


    public DatabaseService(DatabaseRepository databaseRepository, SystemExit systemExit) {
        this.databaseRepository = databaseRepository;
        this.systemExit = systemExit;
    }

    public Boolean specimenCodeExists(String specimenCode) throws SQLException {
        return databaseRepository.specimenCodeExists(specimenCode);
    }


    public String findSpeciesNameOrMorphoCodeFromSpecimenCode(String specimenCode) {
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

    public void checkDatabaseConnectivity() throws SQLException {
            specimenCodeExists("Test");
    }
}
