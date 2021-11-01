package com.arilab.utils;

import com.arilab.domain.CtScan;
import com.arilab.repository.CtScanRepository;
import com.arilab.repository.DatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CtScanMigrator {

    private static final Logger logger = LoggerFactory.getLogger(CtScanMigrator.class);
    private FileUtils fileUtils;
    private DatabaseRepository databaseRepository;
    private CtScanRepository ctScanRepository;

    public CtScanMigrator(FileUtils fileUtils, DatabaseRepository databaseRepository, CtScanRepository ctScanRepository) {
        this.fileUtils = fileUtils;
        this.databaseRepository = databaseRepository;
        this.ctScanRepository = ctScanRepository;
    }


    public void migrateScan(CtScan ctScan, Connection connection, Boolean dummyMigrationFlag) throws SQLException, IOException {

        ctScanRepository.insertCtScan(ctScan, dummyMigrationFlag);
        fileUtils.migrateFolder(ctScan, dummyMigrationFlag);
        ctScan.setMigrated(true);
    }
}



