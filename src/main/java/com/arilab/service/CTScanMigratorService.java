package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.utils.Config;
import com.arilab.utils.CtScanMigrator;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class CTScanMigratorService {

    private FileUtils fileUtils = new FileUtils();


    private CtScanMigrator ctScanMigrator = new CtScanMigrator();
    private static final Logger logger = LoggerFactory.getLogger(CTScanMigratorService.class);
    Config config = Config.getInstance();


    public void migrateScans(List<CtScan> scanList, String fileOutput, Boolean dummyMigrationFlag) {
        Iterator<CtScan> ctScanIterator = scanList.iterator();

        try (Connection connection = DriverManager.getConnection(config.dbhost,
                config.username,
                config.password)) {
            while (ctScanIterator.hasNext()) {
                CtScan ctScan = ctScanIterator.next();
                ctScanMigrator.migrateScan(ctScan, connection, dummyMigrationFlag);
            }
        } catch (SQLException sqlException) {
            logger.error("Exception during migration service: " + sqlException.toString());
        } finally {
            fileUtils.writeBeansToFile(scanList, fileOutput);
        }

    }






}
