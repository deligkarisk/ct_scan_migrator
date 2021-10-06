package com.arilab.utils;

import com.arilab.domain.CtScan;
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

    public CtScanMigrator(FileUtils fileUtils, DatabaseRepository databaseRepository) {
        this.fileUtils = fileUtils;
        this.databaseRepository = databaseRepository;
    }

    private static String insertSql = "INSERT INTO public.ctscans (specimen_code, ct_scan_note, ethanol_conc, wet, body_part, " +
            "special_identifier, folder_location, scan_user, scan_reason, staining, " +
            "antscan, antscan_code, model, dry_method, migrated_from) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";


    public void migrateScan(CtScan ctScan, Connection connection, Boolean dummyMigrationFlag)  {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            assignScanValuesToPreparedStatement(preparedStatement, ctScan);
            logger.info("Executing SQL statement: " + preparedStatement.toString());
            if (!dummyMigrationFlag) {
                preparedStatement.execute();
            }
            fileUtils.migrateFolder(ctScan, dummyMigrationFlag);
            ctScan.setMigrated(true);
        } catch (SQLException | IOException exception) {
            logger.error("Exception caught during migration: {}", exception.toString());
            ctScan.setMigrated(false);
            ctScan.setMigrationException(exception.toString().substring(0,200));
        }
    }


    public void assignScanValuesToPreparedStatement(PreparedStatement preparedStatement, CtScan ctScan) throws SQLException {
        databaseRepository.setStringWrapper(1, ctScan.getSpecimenCode(), preparedStatement);
        databaseRepository.setStringWrapper(2, ctScan.getCtScanNote(), preparedStatement);
        databaseRepository.setStringWrapper(3, ctScan.getEthanolConcentration(), preparedStatement);
        databaseRepository.setStringWrapper(4, ctScan.getWet(), preparedStatement);
        databaseRepository.setStringWrapper(5, ctScan.getBodyPart(), preparedStatement);
        databaseRepository.setStringWrapper(6, ctScan.getSpecialIdentifier(), preparedStatement);
        databaseRepository.setStringWrapper(7, stripFirstPart(ctScan.getNewFolderPath()), preparedStatement);
        databaseRepository.setStringWrapper(8, ctScan.getScanUser(), preparedStatement);
        databaseRepository.setStringWrapper(9, ctScan.getScanReason(), preparedStatement);
        databaseRepository.setStringWrapper(10, ctScan.getStaining(), preparedStatement);
        databaseRepository.setStringWrapper(11, ctScan.getAntscan(), preparedStatement);
        databaseRepository.setStringWrapper(12, ctScan.getAntscanCode(), preparedStatement);
        databaseRepository.setStringWrapper(13, ctScan.getModel(), preparedStatement);
        databaseRepository.setStringWrapper(14, ctScan.getDryMethod(), preparedStatement);
        databaseRepository.setStringWrapper(15, ctScan.getFolderLocation(), preparedStatement);

    }


    private String stripFirstPart(String folderPath) {
        // Removes the /mnt/bucket part of the folder to leave the standardized one, starting with CTScans/...
        int index = folderPath.indexOf("CTScans");
        return folderPath.substring(index);
    }
}
