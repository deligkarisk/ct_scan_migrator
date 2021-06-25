package com.arilab.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DBTool {

    String selectAllSpecimens = "SELECT * FROM public.specimen WHERE specimen_code = ?";
    String selectGenus = "SELECT species.genus_name FROM public.specimen LEFT JOIN species USING(taxon_code) WHERE " +
            "specimen.specimen_code = ?";
    String selectSpeciesName = "SELECT species.species_name FROM public.specimen LEFT JOIN species USING(taxon_code) " +
            "WHERE " + "specimen.specimen_code = ?";
    String selectSubspecies = "SELECT species.subspecies FROM public.specimen LEFT JOIN species USING(taxon_code) " +
            "WHERE " + "specimen.specimen_code = ?";
    String selectMorphoCode = "SELECT species.morpho_code FROM public.specimen LEFT JOIN species USING(taxon_code) " +
            "WHERE " + "specimen.specimen_code = ?";

    Logger logger = LoggerFactory.getLogger(DBTool.class);

    SettingsReader settingsReader;

    public DBTool() {
        settingsReader = new SettingsReader();
    }


    public Boolean specimenCodeExists(String specimenCode)  {
        Boolean exists = null;
        ResultSet resultSet = runSQLQueryOnSpecimenCode(selectAllSpecimens, specimenCode);
        try {
            exists = resultSet.isBeforeFirst();
        } catch (SQLException exception) {
            logger.error("SQL Exception: " + exception.toString());
        }
        return exists;
    }

    public String getGenusFromSpecimenCode(String specimenCode) {
        String genus;
        ResultSet resultSet = runSQLQueryOnSpecimenCode(selectGenus, specimenCode);
        genus = getStringFromResultSet(resultSet, "genus");
        return genus;
    }

    public String getSpeciesNameFromSpecimenCode(String specimenCode) {
        String speciesName;
        ResultSet resultSet = runSQLQueryOnSpecimenCode(selectSpeciesName, specimenCode);
        speciesName = getStringFromResultSet(resultSet, "species_name");
        return speciesName;
    }

    public String getSubspeciesFromSpecimenCode(String specimenCode) {
        String subspecies;
        ResultSet resultSet = runSQLQueryOnSpecimenCode(selectSubspecies, specimenCode);
        subspecies = getStringFromResultSet(resultSet, "subspecies");
        return subspecies;
    }

    public String getMorphoCodeFromSpecimenCode(String specimenCode) {
        String morphoCode;
        ResultSet resultSet = runSQLQueryOnSpecimenCode(selectMorphoCode, specimenCode);
        morphoCode = getStringFromResultSet(resultSet, "morpho_code");
        return morphoCode;
    }

    private String getStringFromResultSet(ResultSet resultSet, String string) {
        String extractedValue = null;
        try {
            extractedValue = resultSet.getString(string);
        } catch (SQLException exception) {
            logger.error("SQL Exception: " + exception);
        }
        return extractedValue;

    }

    public ResultSet runSQLQueryOnSpecimenCode(String query, String specimenCode) {
        try (Connection connection = DriverManager.getConnection(settingsReader.dbHost,
                                                                 settingsReader.credentials.get(0),
                                                                 settingsReader.credentials.get(1));
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, specimenCode);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet;
            }
        } catch (SQLException exception) {
            logger.error("SQL Exception: " + exception.toString());
        }
        return null;
    }


}
