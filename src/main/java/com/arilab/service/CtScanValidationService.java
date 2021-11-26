package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.validator.CtScanValidator;
import com.arilab.domain.validator.error.CtScanErrorModel;
import com.arilab.domain.validator.error.ErrorModel;
import com.arilab.domain.validator.group.ValidatorGroup;

import java.sql.SQLException;
import java.util.Optional;

public class CtScanValidationService {



    // todo: shall we move this to the CtScanService?
    public ErrorModel validate(ValidatorGroup<CtScanValidator> validatorGroup, CtScan ctScan) throws SQLException {

        CtScanErrorModel errors = new CtScanErrorModel(ctScan.getFolderLocation());

        for (CtScanValidator ctScanValidator : validatorGroup.getValidators()) {
            Optional<String> result = ctScanValidator.validate(ctScan);
            if (result.isPresent()) {
                errors.addError(result.get());
            }
        }
        return errors;
    }

}
