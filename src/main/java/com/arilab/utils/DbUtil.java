package com.arilab.utils;

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

    Logger logger = LoggerFactory.getLogger(DbUtil.class);

    SettingsReader settingsReader;

    public DbUtil() {
        settingsReader = new SettingsReader();
    }


    public Boolean specimenCodeExists(String specimenCode) {
        try (Connection connection = DriverManager.getConnection(settingsReader.dbHost,
                                                                 settingsReader.credentials.get(0),
                                                                 settingsReader.credentials.get(1));
             PreparedStatement preparedStatement = connection.prepareStatement(selectAllSpecimens)) {
            preparedStatement.setString(1, specimenCode);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.isBeforeFirst();
            }
        } catch (SQLException exception) {
            logger.error("SQL Exception: " + exception);
            System.exit(1);
        }
        return false;
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
        try (Connection connection = DriverManager.getConnection(settingsReader.dbHost,
                                                                 settingsReader.credentials.get(0),
                                                                 settingsReader.credentials.get(1));
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





}
