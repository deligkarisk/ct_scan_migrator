package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BodyPartCtScanValidator implements CtScanValidator {
    @Override
    public Optional<String> validate(CtScan ctScan) {
        List<String> validValues = Arrays.asList("Head", "Whole_body", "Legs", "Thorax",
                "Abdomen");
        if (ctScan.getBodyPart() == null) {
            return Optional.of("Body part should not be empty.");
        }
        if (validValues.contains(ctScan.getBodyPart())) {
            return Optional.empty();
        }
        return Optional.of("Wrong value in the Body part field.");
    }
}
