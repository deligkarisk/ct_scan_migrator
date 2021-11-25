package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.domain.validator.CtScanCollectionValidator;
import com.arilab.domain.validator.CtScanValidator;
import com.arilab.domain.validator.error.ErrorModel;
import com.arilab.domain.validator.group.ValidatorGroup;
import com.arilab.flowcontroller.UniqueFoldersChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CtScanCollectionService {

    private static Logger logger = LoggerFactory.getLogger(CtScanCollectionService.class);

    CTScanService ctScanService;
    UniqueFoldersChecker uniqueFoldersChecker;
    CtScanValidationService ctScanValidationService;
    CtScanCollectionValidationService ctScanCollectionValidationService;


    public CtScanCollectionService(CTScanService ctScanService,
                                   UniqueFoldersChecker uniqueFoldersChecker,
                                   CtScanValidationService ctScanValidationService,
                                   CtScanCollectionValidationService ctScanCollectionValidationService) {
        this.ctScanService = ctScanService;
        this.uniqueFoldersChecker = uniqueFoldersChecker;
        this.ctScanValidationService = ctScanValidationService;
        this.ctScanCollectionValidationService = ctScanCollectionValidationService;
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


 /*   public void validateScanData(CtScanCollection ctScanCollection) throws SQLException {
        // todo: replace with Strategy pattern
        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            logger.info("Validating scan " + ctScan.getSpecimenCode() + ", " + ctScan.getFolderLocation());
            ctScanService.validateScanData(ctScan);
        }
    }*/

    // todo: can the below two methods be refactored to an interface? We need the returned value to always be a
    //  List<ErrorModel>.
    public List<ErrorModel> validateCollectionAtScanLevel(ValidatorGroup<CtScanValidator> validatorGroup,
                                                          CtScanCollection ctScanCollection) throws SQLException {
        List<ErrorModel> collectionErrors = new ArrayList<>();

        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ErrorModel errors = ctScanValidationService.validate(validatorGroup, ctScan);
            if (errors.hasErrors()) {
                collectionErrors.add(errors);
            }
        }
        return collectionErrors;
    }

    public List<ErrorModel> validateCollection(ValidatorGroup<CtScanCollectionValidator> validatorGroup,
                                               CtScanCollection ctScanCollection) throws SQLException {
        List<ErrorModel> collectionErrors = new ArrayList<>();
       ErrorModel errors = ctScanCollectionValidationService.validate(validatorGroup, ctScanCollection);

       if (errors.hasErrors()) {
           collectionErrors.add(errors);
        }

       return collectionErrors;
    }
    public void findStandardizedFolderNames(CtScanCollection ctScanCollection) {
        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ctScanService.setNewFolderPaths(ctScan);
        }
    }
}
