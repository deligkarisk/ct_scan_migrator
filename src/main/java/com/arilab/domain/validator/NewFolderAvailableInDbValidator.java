package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.util.Optional;

public class NewFolderAvailableInDbValidator implements Validator {
    @Override
    public Optional<String> validate(CtScan ctScan) {
        return Optional.empty();
    }
}
