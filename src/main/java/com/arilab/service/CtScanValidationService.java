package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.validator.Validator;
import com.arilab.domain.validator.ValidatorGroup;

import java.util.ArrayList;
import java.util.Optional;

public class CtScanValidationService {

    public ArrayList<String> validate(ValidatorGroup validatorGroup, CtScan ctScan) {

        ArrayList<String> errors = new ArrayList<>();

        for (Validator validator : validatorGroup.getValidators()) {
            Optional<String> result = validator.validate(ctScan);
            if (result.isPresent()) {
                errors.add(result.get());
            }
        }
        return errors;
    }
}
