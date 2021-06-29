package com.arilab;

import com.arilab.domain.CTScanValidator;
import com.arilab.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static final String DATA_LABEL = "CongsData";
    private static final String FILE_TO_WRITE = "./MigrationOutput" + "_" + DATA_LABEL + ".csv";
    private static final String CTSCAN_DATA_FILE = "./data/CTScansForUploadDev.csv";

            /*"/home/kosmas-deligkaris/repositories/arilabdb" +
            "/202106_Add_Congs_data/SourcesFromPaco" +
            "/CTScansForUpload.csv";
*/
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final CTScanValidator validator = new CTScanValidator();
    private static final FileUtils fileUtils = new FileUtils();
    private static final CTScanUtils ctScanUtils = new CTScanUtils();
    private static final CTScanUtilsService ctScanUtilsService = new CTScanUtilsService();
    private static final CTScanMigrator ctScanMigrator = new CTScanMigrator();
    private static final SettingsReader settingsReader = new SettingsReader();
    private static final DBTool dbTool = new DBTool();

    public static void main(String[] args) {

        logger.info("************************** Starting app **************************");

        if (!preliminaryChecksPassed()) {
            logger.error("Preliminary checks failed, aborting operation...");
            System.exit(1);
        };


        List scansList = fileUtils.getScansFromFile(CTSCAN_DATA_FILE);
        prepareScans(scansList);
        ctScanMigrator.migrateScans(scansList);


    }

    private static void prepareScans(List scansList) {
        int validScans;
        ctScanUtilsService.fixScans(scansList); // standardizes folder locations, among others.

        validScans = validator.validateInputData(scansList); // validates if scans can be migrated, all info is correct.
        if (validScans != scansList.size()) {
            logger.error("Not all scans passed validation of input data, migration will not proceed.");
            System.exit(1);
        }

        ctScanUtilsService.findNewFolderNames(scansList);
        validScans = validator.validateDerivedData(scansList);
        if (validScans != scansList.size()) {
            logger.error("Not all scans passed validation of derived data, migration will not proceed.");
            System.exit(1);
        }


        logger.info("All scans passed validation, with both old and new data, continuing operations...");
        fileUtils.writeBeansToFile(scansList, FILE_TO_WRITE);
    }

    private static Boolean preliminaryChecksPassed() {
        return checkBucketConnectivity() && checkDBConnectivity();
    }

    private static Boolean checkBucketConnectivity() {
        Boolean oldBucketFolderIsMounted = Files.exists(Paths.get(settingsReader.getPrependBucketStringOld()));
        Boolean newBucketFolderIsMounted = Files.exists(Paths.get(settingsReader.getPrependBucketStringNew()));

        if (!(oldBucketFolderIsMounted && newBucketFolderIsMounted)) {
            logger.error("Bucket folders are not mounted, aborting operations...");
            System.exit(1);
        }


        Boolean canWriteToNewFolderLocation = false;
        try {
            Files.deleteIfExists(Paths.get("./testFile"));
            Files.createFile(Paths.get("./testFile"));
            canWriteToNewFolderLocation = true;
        } catch (IOException exception) {
            logger.error("Cannot write to new bucket location, aborting..." + exception);
            System.exit(1);
        }
        return (oldBucketFolderIsMounted && newBucketFolderIsMounted && canWriteToNewFolderLocation);
    }

    private static Boolean checkDBConnectivity() {
        return !(dbTool.specimenCodeExists("TEST"));
    }


}


