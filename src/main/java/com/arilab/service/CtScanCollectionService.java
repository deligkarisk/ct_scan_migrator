package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.domain.validator.CtScanCollectionValidator;
import com.arilab.domain.validator.CtScanValidator;
import com.arilab.domain.validator.error.CtScanCollectionErrorModel;
import com.arilab.domain.validator.error.CtScanErrorModel;
import com.arilab.domain.validator.error.ErrorModel;
import com.arilab.domain.validator.group.ValidatorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CtScanCollectionService {

    private static Logger logger = LoggerFactory.getLogger(CtScanCollectionService.class);

    CTScanService ctScanService;

    public CtScanCollectionService(CTScanService ctScanService
                                  ) {
        this.ctScanService = ctScanService;
    }

    public void preprocessData(CtScanCollection ctScanCollection) {
        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();

        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            logger.info("Working on scan: " + ctScan.getFolderLocation());
            ctScanService.preprocessScanFolderLocation(ctScan);
            ctScanService.updateDicomFolder(ctScan);
            ctScanService.updateTimestamp(ctScan);
        }
    }


    // todo: can the below two methods be refactored to an interface? We need the returned value to always be a
    //  List<ErrorModel>.

    // todo: write tests for this method
    public List<ErrorModel> validateCollectionAtScanLevel(ValidatorGroup<CtScanValidator> validatorGroup,
                                                          CtScanCollection ctScanCollection) throws SQLException {
        List<ErrorModel> collectionErrors = new ArrayList<>();

        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ErrorModel errors = internalValidateCollectionAtScanLevel(validatorGroup, ctScan);
            if (errors.hasErrors()) {
                collectionErrors.add(errors);
            }
        }
        return collectionErrors;
    }

    private ErrorModel internalValidateCollectionAtScanLevel(ValidatorGroup<CtScanValidator> validatorGroup,
                                                        CtScan ctScan) throws SQLException {

        CtScanErrorModel errors = new CtScanErrorModel(ctScan.getFolderLocation());

        for (CtScanValidator ctScanValidator : validatorGroup.getValidators()) {
            Optional<String> result = ctScanValidator.validate(ctScan);
            if (result.isPresent()) {
                errors.addError(result.get());
            }
        }
        return errors;
    }


    // todo: write tests for this method
    public List<ErrorModel> validateCollection(ValidatorGroup<CtScanCollectionValidator> validatorGroup,
                                               CtScanCollection ctScanCollection) throws SQLException {
        List<ErrorModel> collectionErrors = new ArrayList<>();
       ErrorModel errors = internalValidateCollection(validatorGroup, ctScanCollection);

       if (errors.hasErrors()) {
           collectionErrors.add(errors);
        }

       return collectionErrors;
    }

    private ErrorModel internalValidateCollection(ValidatorGroup<CtScanCollectionValidator> validatorGroup,
                          CtScanCollection ctScanCollection) throws SQLException {

        CtScanCollectionErrorModel ctScanCollectionErrorModel = new CtScanCollectionErrorModel();

        for (CtScanCollectionValidator ctScanCollectionValidator : validatorGroup.getValidators()) {
            Optional<String> result = ctScanCollectionValidator.validate(ctScanCollection);
            if (result.isPresent()) {
                ctScanCollectionErrorModel.addError(result.get());
            }
        }
        return ctScanCollectionErrorModel;
    }


    public void findStandardizedFolderNames(CtScanCollection ctScanCollection) {
        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ctScanService.setNewFolderPaths(ctScan);
        }
    }
}
