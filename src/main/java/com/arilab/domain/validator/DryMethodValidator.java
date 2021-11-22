package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.util.Optional;

public class DryMethodValidator implements Validator{
    // todo: replace with enum
    @Override
    public Optional<String> validate(CtScan ctScan) {
        if (ctScan.getDryMethod() == null) {
            return Optional.empty();
        }
        if (ctScan.getDryMethod().equals("Pin") || ctScan.getDryMethod().equals(
                "Amber") || ctScan.getDryMethod().equals("Resin") || ctScan.getDryMethod().equals("CPD")
                || ctScan.getDryMethod().equals("Freeze_dried")) {
            return Optional.empty();
        }
        return Optional.of("Dry field is not correct.");
    }
}
