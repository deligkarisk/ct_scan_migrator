package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.domain.CtScanCollectionValidator;
import com.arilab.domain.validator.ValidatorGroup;
import com.arilab.flowcontroller.UniqueFoldersChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CtScanCollectionService {

    private static Logger logger = LoggerFactory.getLogger(CtScanCollectionService.class);

    CTScanService ctScanService;
    CtScanCollectionValidator ctScanCollectionValidator;
    UniqueFoldersChecker uniqueFoldersChecker;
    CtScanValidationService ctScanValidationService;


    public CtScanCollectionService(CTScanService ctScanService, CtScanCollectionValidator ctScanCollectionValidator,
                                   UniqueFoldersChecker uniqueFoldersChecker, CtScanValidationService ctScanValidationService) {
        this.ctScanService = ctScanService;
        this.ctScanCollectionValidator = ctScanCollectionValidator;
        this.uniqueFoldersChecker = uniqueFoldersChecker;
        this.ctScanValidationService = ctScanValidationService;
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

    public HashMap<String, ArrayList<String>> validateCollection(ValidatorGroup validatorGroup,
                                                       CtScanCollection ctScanCollection) throws SQLException {
        HashMap<String, ArrayList<String>> collectionErrors = new HashMap<>();
        ArrayList<String> ctScanErrors = new ArrayList<>();


        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ctScanErrors = ctScanValidationService.validate(validatorGroup, ctScan);
            collectionErrors.put(ctScan.getFolderLocation(), ctScanErrors);
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


    public void validateStandardizedFolderNames(CtScanCollection ctScanCollection) throws SQLException {
        // todo: replace with Strategy pattern
        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ctScanService.validateStandardizedFolder(ctScan);
        }
    }

    public void validateAllFoldersUniqueInCollection(CtScanCollection ctScanCollection) {
        // todo: replace with Strategy pattern
        Boolean areAllFoldersUnique = ctScanCollectionValidator.areAllFoldersUniqueInCollection(ctScanCollection);
        uniqueFoldersChecker.check(areAllFoldersUnique);
    }





}
