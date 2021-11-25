package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StainingCtScanValidator implements CtScanValidator {
    // todo: replace with enum
    @Override
    public Optional<String> validate(CtScan ctScan) {
        List<String> stainingValues = Arrays.asList("Iodine", "PTA", "Osmium", "No staining");
        if (stainingValues.contains(ctScan.getStaining())) {
            return Optional.empty();
        } else {
            return Optional.of("Staining field is wrong.");
        }
    }
}
