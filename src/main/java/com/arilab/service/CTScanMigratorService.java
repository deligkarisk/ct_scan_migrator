package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.utils.Config;
import com.arilab.utils.CtScanMigrator;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class CTScanMigratorService {

    private FileUtils fileUtils;
    Config config;
    private CtScanMigrator ctScanMigrator;


    private static final Logger logger = LoggerFactory.getLogger(CTScanMigratorService.class);


    public CTScanMigratorService(FileUtils fileUtils, CtScanMigrator ctScanMigrator, Config config) {
        this.fileUtils = fileUtils;
        this.ctScanMigrator = ctScanMigrator;
        this.config = config;
    }

    public void migrateScans(List<CtScan> scanList, String fileOutput, Boolean dummyMigrationFlag) throws SQLException, IOException {
        Iterator<CtScan> ctScanIterator = scanList.iterator();

        try (Connection connection = DriverManager.getConnection(config.getDbhost(),
                config.getUsername(),
                config.getPassword())) {
            while (ctScanIterator.hasNext()) {
                CtScan ctScan = ctScanIterator.next();
                ctScanMigrator.migrateScan(ctScan, connection, dummyMigrationFlag);
            }
        }

    }

}
