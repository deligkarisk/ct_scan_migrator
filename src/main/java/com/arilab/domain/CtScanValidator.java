package com.arilab.domain;

import com.arilab.service.DatabaseService;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CtScanValidator {

    private static Logger logger = LoggerFactory.getLogger(CtScanValidator.class);
    DatabaseService databaseService;
    FileUtils fileUtils;

    public CtScanValidator(DatabaseService databaseService, FileUtils fileUtils) {
        this.databaseService = databaseService;
        this.fileUtils= fileUtils;
    }






    public boolean standardizedFolderIsAvailable(CtScan ctScan) {
        return !fileUtils.folderExists(Paths.get(ctScan.getNewFolderPath()));
    }

}
