package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.util.Optional;

public class WetDryCombinationCtScanValidator implements CtScanValidator {
    @Override
    public Optional<String> validate(CtScan ctScan) {

        if (ctScan.getWet().equals(
                "Yes") && ctScan.getDryMethod() == null && ctScan.getEthanolConcentration() != null) {
            return Optional.empty();
        }

        if (ctScan.getWet().equals("No") && ctScan.getDryMethod() != null && ctScan.getEthanolConcentration().equals("No ethanol used")) {
            return Optional.empty();
        }
        return Optional.of("Wet/dry combinations not correct.");
    }
}
