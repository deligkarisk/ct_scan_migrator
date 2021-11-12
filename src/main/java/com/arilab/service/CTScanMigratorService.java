package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.repository.CtScanRepository;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class CTScanMigratorService {

    FileUtils fileUtils;
    CtScanRepository ctScanRepository;


    private static final Logger logger = LoggerFactory.getLogger(CTScanMigratorService.class);


    public CTScanMigratorService(FileUtils fileUtils, CtScanRepository ctScanRepository) {
        this.fileUtils = fileUtils;
        this.ctScanRepository = ctScanRepository;
    }

    public void migrateScans(CtScanCollection ctScanCollection, Boolean dummyMigrationFlag) throws SQLException,
            IOException {

        for (CtScan ctScan : ctScanCollection.getCtScans()) {
            ctScanRepository.insertCtScan(ctScan, dummyMigrationFlag);
            migrateScan(ctScan, dummyMigrationFlag);
            ctScan.setMigrated(true);
        }
    }


    private void migrateScan(CtScan ctScan, Boolean dummyMigrationFlag) throws IOException {
        fileUtils.moveMainFolder(ctScan, dummyMigrationFlag);

        if (ctScan.getDicomFolderLocation() != null) {
            fileUtils.moveDicomFolder(ctScan, dummyMigrationFlag);
        }
    }
}
