package com.arilab;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.reader.SourceReader;
import com.arilab.service.CTScanMigratorService;
import com.arilab.service.CTScanService;
import com.arilab.service.CtScanUtilsService;
import com.arilab.service.CtScanValidatorService;
import com.arilab.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
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
    private static final CTScanService ctScanService = new CTScanService();
    public static final SourceReader sourceReader = new SourceReader();



    public static void main(String[] args) {

        logger.info("************************** Starting app **************************");


        if (args.length < 2) {
            System.out.println("Enter at least the datafile and the datalabel.");
            System.exit(1);
        }

        if (args.length == 3) {
            if (args[2].equals("--do-migration")) {
                DUMMY_EXECUTION = false;
            } else {
                System.out.println("Erroneous third argument, exiting." + args[1]);
                System.exit(1);
            }
        }

        String CTSCAN_DATA_FILE = args[0];
        dataLabel = args[1];
        outputFile = "./MigrationOutput" + "_" + dataLabel + ".csv";
        failedValidationOutput = "./ValidationFailed" + "_" + dataLabel + ".csv";










        logger.info("Reading data from: " + CTSCAN_DATA_FILE);


        if (!preliminaryChecksPassed()) {
            logger.error("Preliminary checks failed, aborting operation...");
            System.exit(1);
        }

        List scansList = sourceReader.readScans(CTSCAN_DATA_FILE);


        Iterator<CtScan> ctScanIterator = scansList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            logger.info("Working on scan: " + ctScan.getFolderLocation());
            ctScanService.preprocessScanFolderLocation(ctScan);
            ctScanService.updateDicomFolder(ctScan);
            ctScanService.updateTimestamp(ctScan);


        }

        //TODO: Refactor validations
        ctScanValidatorService.validateScanData(scansList, failedValidationOutput);
        // TODO: Add a new class for deciding whether to continue or not. (remove this decision from the validator)


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


