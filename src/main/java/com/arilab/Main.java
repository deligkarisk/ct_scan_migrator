package com.arilab;

import com.arilab.domain.CtScanValidator;
import com.arilab.service.CTScanMigratorService;
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

    private static  String dataLabel;
    private static String outputFile;
    private static  String failedValidationOutput;

    private static Boolean DUMMY_EXECUTION = true;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final CtScanValidator validator = new CtScanValidator();
    private static final CtScanValidatorService ctScanValidatorService = new CtScanValidatorService();
    private static final FileUtils fileUtils = new FileUtils();
    private static final CtScanUtils ctScanUtils = new CtScanUtils();
    private static final CtScanUtilsService ctScanUtilsService = new CtScanUtilsService();
    private static final CTScanMigratorService ctScanMigratorService = new CTScanMigratorService();
    private static final DbUtil dbUtil = new DbUtil();
    private static final Config config = Config.getInstance();


    public static void main(String[] args) {

        logger.info("************************** Starting app **************************");
        if (args.length < 2) {
            System.out.println("Please enter the file name to read from, as well as a label for the outputs.");
            System.exit(1);
        }

        String CTSCAN_DATA_FILE = args[0];
        dataLabel = args[1];
        outputFile = "./MigrationOutput" + "_" + dataLabel + ".csv";
        failedValidationOutput = "./ValidationFailed" + "_" + dataLabel + ".csv";



        if (args.length >= 3) {
            if (args[2].equals("--do-migration")) {
                DUMMY_EXECUTION = false;
            } else {
                System.out.println("Erroneous third argument, exiting." + args[1]);
                System.exit(1);
            }
        }






        logger.info("Reading data from: " + CTSCAN_DATA_FILE);


        if (!preliminaryChecksPassed()) {
            logger.error("Preliminary checks failed, aborting operation...");
            System.exit(1);
        }

        List scansList = fileUtils.getScansFromFile(CTSCAN_DATA_FILE);
        ctScanUtilsService.preProcessScans(scansList);
        ctScanValidatorService.validateScanData(scansList, failedValidationOutput);
        ctScanUtilsService.findStandardizedFolderNames(scansList);
        ctScanValidatorService.validateStandardizedFolderNames(scansList, failedValidationOutput);
        ctScanValidatorService.validateUniquenessOfFolders(scansList, failedValidationOutput);
        fileUtils.writeBeansToFile(scansList, outputFile);
        ctScanMigratorService.migrateScans(scansList, outputFile, DUMMY_EXECUTION);


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
        Boolean oldBucketFolderIsMounted = Files.exists(Paths.get(config.sourceDirectory));
        Boolean newBucketFolderIsMounted = Files.exists(Paths.get(config.targetDirectory));
        return ((oldBucketFolderIsMounted && newBucketFolderIsMounted));
    }

    private static Boolean canWriteToBucket() throws IOException {
        Files.deleteIfExists(Paths.get(config.targetDirectory, "testFile"));
        Files.createFile(Paths.get(config.targetDirectory, "testFile"));
        Files.deleteIfExists(Paths.get(config.targetDirectory, "testFile"));
        return true;
    }

    private static Boolean checkDBConnectivity() {
        return !(dbUtil.specimenCodeExists("TEST"));
    }


}


