package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.domain.validator.CtScanCollectionValidator;
import com.arilab.domain.validator.CtScanValidator;
import com.arilab.domain.validator.error.CtScanCollectionErrorModel;
import com.arilab.domain.validator.error.CtScanErrorModel;
import com.arilab.domain.validator.error.ErrorModel;
import com.arilab.domain.validator.group.ValidatorGroup;

import java.sql.SQLException;
import java.util.Optional;

public class CtScanCollectionValidationService {


    // todo: shall we move this to the CtScanCollectionService?
    public ErrorModel validate(ValidatorGroup<CtScanCollectionValidator> validatorGroup, CtScanCollection ctScanCollection) throws SQLException {

        CtScanCollectionErrorModel ctScanCollectionErrorModel = new CtScanCollectionErrorModel();

        for (CtScanCollectionValidator ctScanCollectionValidator : validatorGroup.getValidators()) {
            Optional<String> result = ctScanCollectionValidator.validate(ctScanCollection);
            if (result.isPresent()) {
                ctScanCollectionErrorModel.addError(result.get());
            }
        }
        return ctScanCollectionErrorModel;
    }

}
