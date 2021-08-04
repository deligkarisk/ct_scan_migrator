package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.utils.CtScanMigrator;
import com.arilab.utils.SettingsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class CTScanMigratorService {


    private CtScanMigrator ctScanMigrator = new CtScanMigrator();
    private static final Logger logger = LoggerFactory.getLogger(CTScanMigratorService.class);
    SettingsReader settingsReader = new SettingsReader();


    public void migrateScans(List<CtScan> scanList) {
        Iterator<CtScan> ctScanIterator = scanList.iterator();

        try (Connection connection = DriverManager.getConnection(settingsReader.dbHost,
                settingsReader.credentials.get(0),
                settingsReader.credentials.get(1));) {
            while (ctScanIterator.hasNext()) {
                CtScan ctScan = ctScanIterator.next();
                ctScanMigrator.migrateScan(ctScan, connection);
                fileUtils.writeBeansToFile(scansList, FILE_TO_WRITE); //TODO: Fix this as well.
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            //TODO: if it fails, including for SQL or IO exceptio
        }

    }






}
