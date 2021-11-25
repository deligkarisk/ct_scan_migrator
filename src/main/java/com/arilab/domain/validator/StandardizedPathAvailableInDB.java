package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import com.arilab.service.CTScanService;

import java.sql.SQLException;
import java.util.Optional;

public class StandardizedPathAvailableInDB implements CtScanValidator {

    CTScanService ctScanService;

    public StandardizedPathAvailableInDB(CTScanService ctScanService) {
        this.ctScanService = ctScanService;
    }

    @Override
    public Optional<String> validate(CtScan ctScan) throws SQLException {
        Boolean standardizedPathIsAvailInDb = !ctScanService.ctScanFolderExists(ctScan.getNewFolderPath());

        if  (standardizedPathIsAvailInDb) {
            return Optional.empty();
        }

        return Optional.of("Standardized path is not available in the database.");

    }
}
