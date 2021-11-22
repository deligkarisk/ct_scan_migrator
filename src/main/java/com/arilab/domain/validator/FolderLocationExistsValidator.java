package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FolderLocationExistsValidator implements Validator {
    @Override
    public Optional<String> validate(CtScan ctScan) {
        Path location = Paths.get(ctScan.getFolderLocation());
        if (Files.exists(location)) {
            return Optional.empty();
        } else {
            return Optional.of("Folder location does not exist.");
        }
    }
}
