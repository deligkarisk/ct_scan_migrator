package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import com.arilab.service.DatabaseService;

import java.sql.SQLException;
import java.util.Optional;

public class SpecimenCodeExistsCtScanValidator implements CtScanValidator {

    DatabaseService databaseService;

    public SpecimenCodeExistsCtScanValidator(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public Optional<String> validate(CtScan ctScan) throws SQLException {

        if (databaseService.specimenCodeExists(ctScan.getSpecimenCode())) {
            return Optional.empty();
        } else {
            return Optional.of("Specimen code does not exist in the database.");
        }

    }
}
