package com.arilab.utils;

import com.arilab.domain.CtScan;
import com.arilab.service.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CtScanUtils {

    private static Logger logger = LoggerFactory.getLogger(CtScanUtils.class);
    private DatabaseService databaseService;
    private  PathUtils pathUtils;
    private Config config;


    public CtScanUtils(DatabaseService databaseService, PathUtils pathUtils, Config config) {
        this.databaseService = databaseService;
        this.pathUtils = pathUtils;
        this.config = config;
    }

    public String findDicomFolderLocation(CtScan ctScan) {

            String folderLocation = ctScan.getFolderLocation();
            String foundLocation = null;

            // Try to find the dicom folder based on settings.
            String dicomFolderLocation = pathUtils.generateDicomPotentialFolder(folderLocation, config.getDicomLevelsUp(), config.getDicomAppendString());
            if (Files.exists(Paths.get(dicomFolderLocation))) {
                foundLocation = dicomFolderLocation;

            } else {
                // retry with all characters of appendString to lower case.
                dicomFolderLocation = pathUtils.generateDicomPotentialFolder(folderLocation, config.getDicomLevelsUp(), config.getDicomAppendString().toLowerCase(Locale.ROOT));
                if (Files.exists(Paths.get(dicomFolderLocation))) {
                    foundLocation = dicomFolderLocation;
                }
            }
            return foundLocation;
    }



    public String createTimestampFromScanDate(CtScan ctScan) {
        String timestamp = null;
        try {
            SimpleDateFormat sdf = convertToSimpleDateFormat("yyyy-mm-dd");
            timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
        } catch (ParseException exception) {
            try {
                SimpleDateFormat sdf = convertToSimpleDateFormat("yyyy-mm");
                timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
            } catch (ParseException e) {
                try {
                    SimpleDateFormat sdf = convertToSimpleDateFormat("yyyy");
                    timestamp = sdf.format(sdf.parse(ctScan.getScanDate()));
                } catch (ParseException ignored) {
                }
            }
        }

        return timestamp;
    }


    private SimpleDateFormat convertToSimpleDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        return sdf;
    }





}
