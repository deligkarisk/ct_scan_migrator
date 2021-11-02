package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class CtScanCollectionService {

    private static Logger logger = LoggerFactory.getLogger(CtScanCollectionService.class);
    CTScanService ctScanService;


    public CtScanCollectionService(CTScanService ctScanService) {
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


    public void validateScanData(CtScanCollection ctScanCollection) throws SQLException {
        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            logger.info("Validating scan " + ctScan.getSpecimenCode() + ", " + ctScan.getFolderLocation());
            ctScanService.validateScanData(ctScan);
        }
    }


    public void findStandardizedFolderNames(CtScanCollection ctScanCollection) {
        Iterator<CtScan> ctScanIterator = ctScanCollection.getCtScans().iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ctScanService.findStandardizedFolderName(ctScan);
        }
    }

}
