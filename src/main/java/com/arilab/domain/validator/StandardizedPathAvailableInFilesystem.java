package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;

public class StandardizedPathAvailableInFilesystem implements CtScanValidator {

    @Override
    public Optional<String> validate(CtScan ctScan) throws SQLException {
        boolean folderIsAvailable = !Files.exists(Paths.get(ctScan.getNewFolderPath()));

        if (folderIsAvailable) {
            return Optional.empty();
        }
        return Optional.of("Standardized path is not available in the filesystem.");
    }
}
