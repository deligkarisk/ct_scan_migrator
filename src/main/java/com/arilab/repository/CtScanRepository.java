package com.arilab.repository;

import com.arilab.domain.CtScan;
import com.arilab.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class CtScanRepository {

    private Config config;

    public CtScanRepository(Config config) {
        this.config = config;
    }

    private static Logger logger = LoggerFactory.getLogger(CtScanRepository.class);


    private static String insertSql = "INSERT INTO public.ctscans (specimen_code, ct_scan_note, ethanol_conc, wet, body_part, " +
            "special_identifier, folder_location, scan_user, scan_reason, staining, " +
            "antscan, antscan_code, model, dry_method, migrated_from) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";


    private static String selectFolder = "SELECT folder_location FROM public.ctscans WHERE folder_location = ?";


    public void insertCtScan(CtScan ctScan, Boolean dummyMigrationFlag) throws SQLException {
        try (Connection connection = DriverManager.getConnection(config.getDbhost(),
                config.getUsername(),
                config.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

            assignScanValuesToPreparedStatement(preparedStatement, ctScan);

            logger.info("Executing SQL statement: " + preparedStatement.toString());
            if (!dummyMigrationFlag) {
                preparedStatement.execute();
            }
        }
    }


    public ResultSet findCtScanFolder(String folder) throws SQLException {
        ResultSet queryResult;
        try (Connection connection = DriverManager.getConnection(config.getDbhost(),
                config.getUsername(),
                config.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(selectFolder)) {

            setStringWrapper(1, stripFirstPart(folder), preparedStatement);
             queryResult = preparedStatement.executeQuery();
        }
        return queryResult;
    }


    public void assignScanValuesToPreparedStatement(PreparedStatement preparedStatement, CtScan ctScan) throws SQLException {
        setStringWrapper(1, ctScan.getSpecimenCode(), preparedStatement);
        setStringWrapper(2, ctScan.getCtScanNote(), preparedStatement);
        setStringWrapper(3, ctScan.getEthanolConcentration(), preparedStatement);
        setStringWrapper(4, ctScan.getWet(), preparedStatement);
        setStringWrapper(5, ctScan.getBodyPart(), preparedStatement);
        setStringWrapper(6, ctScan.getSpecialIdentifier(), preparedStatement);
        setStringWrapper(7, stripFirstPart(ctScan.getNewFolderPath()), preparedStatement);
        setStringWrapper(8, ctScan.getScanUser(), preparedStatement);
        setStringWrapper(9, ctScan.getScanReason(), preparedStatement);
        setStringWrapper(10, ctScan.getStaining(), preparedStatement);
        setStringWrapper(11, ctScan.getAntscan(), preparedStatement);
        setStringWrapper(12, ctScan.getAntscanCode(), preparedStatement);
        setStringWrapper(13, ctScan.getModel(), preparedStatement);
        setStringWrapper(14, ctScan.getDryMethod(), preparedStatement);
        setStringWrapper(15, ctScan.getFolderLocation(), preparedStatement);
    }

    private String stripFirstPart(String folderPath) {
        // Removes the /mnt/bucket part of the folder to leave the standardized one, starting with CTScans/...
        int index = folderPath.indexOf("CTScans");
        return folderPath.substring(index);
    }


    public void setIntWrapper(Integer paramSeq, Integer value, PreparedStatement preparedStatement) throws
            SQLException {
        if (value == null) {
            preparedStatement.setNull(paramSeq, java.sql.Types.NULL);
            return;
        }
        preparedStatement.setInt(paramSeq, value);
    }


    public void setStringWrapper(Integer paramSeq, String value, PreparedStatement preparedStatement) throws
            SQLException {
        if (value == null) {
            preparedStatement.setNull(paramSeq, java.sql.Types.NULL);
            return;
        }
        preparedStatement.setString(paramSeq, value);
    }


    public void setFloatWrapper(Integer paramSeq, Float value, PreparedStatement preparedStatement) throws
            SQLException {
        if (value == null) {
            preparedStatement.setNull(paramSeq, java.sql.Types.NULL);
            return;
        }
        preparedStatement.setFloat(paramSeq, value);
    }

}
