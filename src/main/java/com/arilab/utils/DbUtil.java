package com.arilab.utils;

import com.arilab.domain.CtScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DbUtil {

    String selectAllSpecimens = "SELECT * FROM public.specimen WHERE specimen_code = ?";
    String selectGenus = "SELECT species.genus_name FROM public.specimen LEFT JOIN species USING(taxon_code) WHERE " +
            "specimen.specimen_code = ?";
    String selectSpeciesName = "SELECT species.species_name FROM public.specimen LEFT JOIN species USING(taxon_code) " +
            "WHERE " + "specimen.specimen_code = ?";
    String selectSubspecies = "SELECT species.subspecies FROM public.specimen LEFT JOIN species USING(taxon_code) " +
            "WHERE " + "specimen.specimen_code = ?";
    String selectMorphoCode = "SELECT species.morpho_code FROM public.specimen LEFT JOIN species USING(taxon_code) " +
            "WHERE " + "specimen.specimen_code = ?";
    String selectFolder = "SELECT folder_location FROM public.ctscans WHERE folder_location = ?";

    Logger logger = LoggerFactory.getLogger(DbUtil.class);

    private Config config = Config.getInstance();

    public DbUtil() {
    }


    public Boolean specimenCodeExists(String specimenCode) {
        try (Connection connection = DriverManager.getConnection(config.dbhost,
                                                                 config.username,
                                                                 config.password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectAllSpecimens)) {
            preparedStatement.setString(1, specimenCode);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.isBeforeFirst();
            }
        } catch (SQLException exception) {
            logger.error("SQL Exception: " + exception.getMessage());
            System.exit(1);
        }
        return false;
    }


    public Boolean ctScanFolderExists(String folder) {
        Boolean folderExists = null;
        try (Connection connection = DriverManager.getConnection(config.dbhost,
                config.username,
                config.password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectFolder)) {
            setStringWrapper(1, folder, preparedStatement);
            folderExists = preparedStatement.executeQuery().isBeforeFirst();
        } catch (SQLException sqlException) {
            logger.error("SQL Exception: " + sqlException);
            System.exit(1);
        }

        return folderExists;

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
        try (Connection connection = DriverManager.getConnection(config.dbhost,
                                                                 config.username,
                                                                 config.password);
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


    public void executeInsertSqlCommand(PreparedStatement preparedStatement){
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


    public void setIntWrapper (Integer paramSeq, Integer value, PreparedStatement preparedStatement ) throws
            SQLException {
        if (value == null) {
            preparedStatement.setNull(paramSeq, java.sql.Types.NULL);
            return;
        }
        preparedStatement.setInt(paramSeq, value);
    }


    public void setStringWrapper (Integer paramSeq, String value, PreparedStatement preparedStatement ) throws
            SQLException {
        if (value == null) {
            preparedStatement.setNull(paramSeq, java.sql.Types.NULL);
            return;
        }
        preparedStatement.setString(paramSeq, value);
    }


    public void setFloatWrapper (Integer paramSeq, Float value, PreparedStatement preparedStatement ) throws
            SQLException {
        if (value == null) {
            preparedStatement.setNull(paramSeq, java.sql.Types.NULL);
            return;
        }
        preparedStatement.setFloat(paramSeq, value);
    }






}
