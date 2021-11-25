package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.util.Optional;

public class ModelCtScanValidator implements CtScanValidator {
    // todo: replace with enum
    @Override
    public Optional<String> validate(CtScan ctScan) {
        if (ctScan.getModel().equals("Ants")) {
            return Optional.empty();
        } else {
            return Optional.of("Model is not Ants.");
        }
    }
}
