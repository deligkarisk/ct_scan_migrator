package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class DicomFolderNotInMainFolderValidator implements Validator {
    @Override
    public Optional<String> validate(CtScan ctScan) {

        if (ctScan.getDicomFolderLocation() == null) {
            return Optional.empty();
        }


        Path dicomPath = Paths.get(ctScan.getDicomFolderLocation());
        Path mainDataPath = Paths.get(ctScan.getFolderLocation());


        if (dicomPath.startsWith(mainDataPath)) {
            return Optional.of("Dicom folder path entered is a child of main folder. Please remove the dicom folder " +
                    "value.");
        } else {
            return Optional.empty();
        }
    }
}
