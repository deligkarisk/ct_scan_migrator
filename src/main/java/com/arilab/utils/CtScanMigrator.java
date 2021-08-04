package com.arilab.utils;

import com.arilab.domain.CtScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class CtScanMigrator {

    private static final Logger logger = LoggerFactory.getLogger(CtScanMigrator.class);
    private FileUtils fileUtils = new FileUtils();
    private DbUtil dbUtil = new DbUtil();

    private static String insertSql = "INSERT INTO public.ctscans (specimen_code, ct_scan_note, ethanol_conc, wet, body_part, " +
            "special_identifier, folder_location, scan_user, scan_reason, staining, " +
            "antscan, antscan_code, model, dry_method, scan_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    SettingsReader settingsReader = new SettingsReader();
    CtScanUtils ctScanUtils = new CtScanUtils();


    public void migrateScan(CtScan ctScan, Connection connection)  {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            assignScanValuesToPreparedStatement(preparedStatement, ctScan);
            preparedStatement.execute();
            fileUtils.migrateFolder(ctScan);
            ctScan.setMigrated(true);
        } catch (SQLException | IOException exception) {
            logger.error("Exception caught during migration: {}", exception.toString());
            ctScan.setMigrated(false);
            ctScan.setMigrationException(exception.toString().substring(0,200));
        }
    }


    public void assignScanValuesToPreparedStatement(PreparedStatement preparedStatement, CtScan ctScan) throws SQLException {
        dbUtil.setStringWrapper(1, ctScan.getSpecimenCode(), preparedStatement);
        dbUtil.setStringWrapper(2, ctScan.getCtScanNote(), preparedStatement);
        dbUtil.setStringWrapper(3, ctScan.getEthanolConcentration(), preparedStatement);
        dbUtil.setStringWrapper(4, ctScan.getWet(), preparedStatement);
        dbUtil.setStringWrapper(5, ctScan.getBodyPart(), preparedStatement);
        dbUtil.setStringWrapper(6, ctScan.getSpecialIdentifier(), preparedStatement);
        dbUtil.setStringWrapper(7, ctScan.getNewFolderPath(), preparedStatement);
        dbUtil.setStringWrapper(8, ctScan.getScanUser(), preparedStatement);
        dbUtil.setStringWrapper(9, ctScan.getScanReason(), preparedStatement);
        dbUtil.setStringWrapper(10, ctScan.getStaining(), preparedStatement);
        dbUtil.setStringWrapper(11, ctScan.getAntscan(), preparedStatement);
        dbUtil.setStringWrapper(12, ctScan.getAntscanCode(), preparedStatement);
        dbUtil.setStringWrapper(13, ctScan.getModel(), preparedStatement);
        dbUtil.setStringWrapper(14, ctScan.getDryMethod(), preparedStatement);
        dbUtil.setStringWrapper(15, ctScan.getScanDate(), preparedStatement);
    }


}
