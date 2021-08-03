package com.arilab;

import com.arilab.domain.CtScanValidator;
import com.arilab.service.CtScanUtilsService;
import com.arilab.service.CtScanValidatorService;
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
    private static final String FAILED_FILE_TO_WRITE = "./MigrationOutputFAILED" + "_" + DATA_LABEL + ".csv";
    private static final String CTSCAN_DATA_FILE = "./testdata/CTScansForUploadDev.csv";

    /*"/home/kosmas-deligkaris/repositories/arilabdb" +
    "/202106_Add_Congs_data/SourcesFromPaco" +
    "/CTScansForUpload.csv";
*/
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final CtScanValidator validator = new CtScanValidator();
    private static final CtScanValidatorService ctScanValidatorService = new CtScanValidatorService();
    private static final FileUtils fileUtils = new FileUtils();
    private static final CtScanUtils ctScanUtils = new CtScanUtils();
    private static final CtScanUtilsService ctScanUtilsService = new CtScanUtilsService();
    private static final CtScanMigrator ctScanMigrator = new CtScanMigrator();
    private static final SettingsReader settingsReader = new SettingsReader();
    private static final DbUtil dbUtil = new DbUtil();

    public static void main(String[] args) {

        logger.info("************************** Starting app **************************");

        if (!preliminaryChecksPassed()) {
            logger.error("Preliminary checks failed, aborting operation...");
            System.exit(1);
        }

        List scansList = fileUtils.getScansFromFile(CTSCAN_DATA_FILE);
        ctScanUtilsService.fixScans(scansList);
        ctScanValidatorService.validateScanData(scansList, FAILED_FILE_TO_WRITE);
        ctScanUtilsService.findStandardizedFolderNames(scansList);
        ctScanValidatorService.validateStandardizedFolderNames(scansList, FAILED_FILE_TO_WRITE);

        fileUtils.writeBeansToFile(scansList, FILE_TO_WRITE);
        ctScanMigrator.migrateScans(scansList);


        logger.info("************************** Finished Execution **************************");
    }



    private static Boolean preliminaryChecksPassed() {
        return checkBucketConnectivity() && checkDBConnectivity();
    }

    private static Boolean checkBucketConnectivity() {
        if (!bucketIsMounted()) {
            logger.error("Bucket folders are not mounted, aborting operations...");
            System.exit(1);
        }

        try {
            Boolean canWriteToNewFolderLocation = canWriteToBucket();
        } catch (IOException exception) {
            logger.error("Cannot write to new bucket location, aborting..." + exception);
            System.exit(1);
        }
        return true;
    }

    private static Boolean bucketIsMounted() {
        Boolean oldBucketFolderIsMounted = Files.exists(Paths.get(settingsReader.getPrependBucketStringOld()));
        Boolean newBucketFolderIsMounted = Files.exists(Paths.get(settingsReader.getPrependBucketStringNew()));
        return ((oldBucketFolderIsMounted && newBucketFolderIsMounted));
    }
    private static Boolean canWriteToBucket() throws IOException {
        Files.deleteIfExists(Paths.get(settingsReader.getPrependBucketStringNew(),"testFile"));
        Files.createFile(Paths.get(settingsReader.getPrependBucketStringNew(),"testFile"));
        Files.deleteIfExists(Paths.get(settingsReader.getPrependBucketStringNew(),"testFile"));
        return true;
    }

    private static Boolean checkDBConnectivity() {
        return !(dbUtil.specimenCodeExists("TEST"));
    }


}


