package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class DicomFolderLocationExistsCtScanValidator implements CtScanValidator {
    @Override
    public Optional<String> validate(CtScan ctScan) {
        if (ctScan.getDicomFolderLocation() == null) {
            return Optional.empty();
        }

        Path dicomPath = Paths.get(ctScan.getDicomFolderLocation());
        if (Files.exists(dicomPath)) {
            return Optional.empty();
        } else {
            return Optional.of("Dicom folder location entered does not exist.");
        }
    }
}
