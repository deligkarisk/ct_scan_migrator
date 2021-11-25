package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EthanolConcCtScanValidator implements CtScanValidator {
    // todo: replace with enum
    @Override
    public Optional<String> validate(CtScan ctScan) {
        List<String> ethanolConcValues = Arrays.asList("No ethanol used", "70%", "95%", "99%", "Not known");
        if (ethanolConcValues.contains(ctScan.getEthanolConcentration())) {
            return Optional.empty();
        } else {
            return Optional.of("Ethanol concentration not correct.");
        }
    }
}
