package com.arilab.repository;

import com.arilab.system.SystemExit;
import com.arilab.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseRepository {

    String selectGenus = "SELECT species.genus_name FROM public.specimen LEFT JOIN species USING(taxon_code) WHERE " +
            "specimen.specimen_code = ?";
    String selectSpeciesName = "SELECT species.species_name FROM public.specimen LEFT JOIN species USING(taxon_code) " +
            "WHERE " + "specimen.specimen_code = ?";
    String selectSubspecies = "SELECT species.subspecies FROM public.specimen LEFT JOIN species USING(taxon_code) " +
            "WHERE " + "specimen.specimen_code = ?";
    String selectMorphoCode = "SELECT species.morpho_code FROM public.specimen LEFT JOIN species USING(taxon_code) " +
            "WHERE " + "specimen.specimen_code = ?";
    String selectAllSpecimens = "SELECT * FROM public.specimen WHERE specimen_code = ?";



    Logger logger = LoggerFactory.getLogger(DatabaseRepository.class);

    private Config config;
    private SystemExit systemExit;


    public DatabaseRepository(SystemExit systemExit, Config config) {
        this.systemExit = systemExit;
        this.config = config;
    }


    public Boolean specimenCodeExists(String specimenCode) throws SQLException {

        try (Connection connection = DriverManager.getConnection(config.getDbhost(),
                config.getUsername(),
                config.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(selectAllSpecimens)) {
            preparedStatement.setString(1, specimenCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.isBeforeFirst();
        }
    }



    private String stripFirstPart(String folderPath) {
        // Removes the /mnt/bucket part of the folder to leave the standardized one, starting with CTScans/...
        int index = folderPath.indexOf("CTScans");
        return folderPath.substring(index);
    }


    public String getGenusFromSpecimenCode(String specimenCode) {
        return runSQLQueryOnSpecimenCode(selectGenus, specimenCode, "genus_name");
    }

    public String getSpeciesNameFromSpecimenCode(String specimenCode) {
        return runSQLQueryOnSpecimenCode(selectSpeciesName, specimenCode, "species_name");
    }

    public String getSubspeciesFromSpecimenCode(String specimenCode) {
        return runSQLQueryOnSpecimenCode(selectSubspecies, specimenCode, "subspecies");
    }

    public String getMorphoCodeFromSpecimenCode(String specimenCode) {
        return runSQLQueryOnSpecimenCode(selectMorphoCode, specimenCode, "morpho_code");
    }


    public String runSQLQueryOnSpecimenCode(String query, String specimenCode, String columnName) {
        try (Connection connection = DriverManager.getConnection(config.getDbhost(),
                config.getUsername(),
                config.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, specimenCode);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getString(columnName);
            }
        } catch (SQLException exception) {
            logger.error("SQL Exception: " + exception);
        }
        return null;
    }


    public void executeInsertSqlCommand(PreparedStatement preparedStatement) {
        try {
            logger.info("Running command {}", preparedStatement);
            Boolean insertSucessfully = preparedStatement.execute();

            if (insertSucessfully) {
                logger.info("Insert successfully completed");
            }

            if (!insertSucessfully) {
                logger.info("Insert could not be executed");
                System.exit(1);
            }

        } catch (SQLException ex) {
            logger.error("Exception caught during insert statement: {}", ex.toString());
            System.exit(1);
        }
    }
}
