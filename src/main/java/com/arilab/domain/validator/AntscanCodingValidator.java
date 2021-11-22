package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.util.Optional;

public class AntscanCodingValidator implements Validator{
    @Override
    public Optional<String> validate(CtScan ctScan) {
        Boolean isCorrect = ((ctScan.getAntscan().equals("No") && ctScan.getAntscanCode() == null) || (ctScan.getAntscan().equals("Yes") && ctScan.getAntscanCode() != null));
        if (isCorrect) {
            return Optional.empty();
        } else {
            return Optional.of("Antscan coding is not correct.");
        }
    }
}
