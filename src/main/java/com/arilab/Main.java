package com.arilab;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.flowcontroller.ArgumentChecker;
import com.arilab.flowcontroller.CtScanDataChecker;
import com.arilab.flowcontroller.DatabaseConnectivityChecker;
import com.arilab.flowcontroller.FilesystemConnectivityChecker;
import com.arilab.reader.SourceReader;
import com.arilab.repository.DatabaseRepository;
import com.arilab.service.*;
import com.arilab.system.SystemExit;
import com.arilab.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import static java.lang.Boolean.FALSE;

public class Main {

    private static Boolean DUMMY_EXECUTION = true;
    private static final String PROPERTIES_FILE = "./config.properties";
    private static final String CREDENTIALS_FILE = "./credentials.properties";
    private static final String OUTPUT_PREPEND = "MigrationOutput";
    private static final String FAILEDOUTPUTPREPEND = "ValidationFailed";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final FileUtils fileUtils = new FileUtils();
    private static final PathUtils pathUtils = new PathUtils();

    private static final Config config = Config.getInstance();
    public static final SourceReader sourceReader = new SourceReader();
    public static final ArgumentChecker argumentChecker = new ArgumentChecker();
    public static final SystemExit systemExit = new SystemExit();
    public static final FileSystemUtils filesystemUtils = new FileSystemUtils();

    private static final DatabaseRepository DATABASE_REPOSITORY = new DatabaseRepository(systemExit, config);
    private static final DatabaseService DATABASE_SERVICE = new DatabaseService(DATABASE_REPOSITORY, systemExit);

    private static final CtScanValidator ctScanValidator = new CtScanValidator(DATABASE_SERVICE, pathUtils);

    private static final CtScanValidatorService ctScanValidatorService = new CtScanValidatorService(ctScanValidator,
            fileUtils, DATABASE_SERVICE);

    private static final CtScanUtils ctScanUtils = new CtScanUtils(DATABASE_SERVICE, pathUtils, config);

    private static final CTScanService ctScanService = new CTScanService(pathUtils, ctScanUtils);

    public static final FilesystemConnectivityChecker filesystemConnectivityChecker =
            new FilesystemConnectivityChecker(config, systemExit, filesystemUtils);

    private static final CtScanUtilsService ctScanUtilsService = new CtScanUtilsService(ctScanUtils, ctScanValidator,
            fileUtils, ctScanValidatorService, config, ctScanService);

    public static final CtScanDataChecker ctScanDataChecker = new CtScanDataChecker(fileUtils, config, systemExit);
    public static final DatabaseConnectivityChecker databaseConnectivityChecker =
            new DatabaseConnectivityChecker(DATABASE_SERVICE, config, systemExit);

    private static final CtScanMigrator ctScanMigrator = new CtScanMigrator(fileUtils, DATABASE_REPOSITORY);
    private static final CTScanMigratorService ctScanMigratorService = new CTScanMigratorService(fileUtils,
            ctScanMigrator, config);



    public static void main(String[] args) {

        argumentChecker.check(args);

        if ((args.length == 3) && (args[2].equals("--do-migration"))) {
            DUMMY_EXECUTION = FALSE;
        }

        logger.info("************************** Starting app **************************");

        Config config =  Config.createInstance(PROPERTIES_FILE, CREDENTIALS_FILE,args[0], args[1], OUTPUT_PREPEND, FAILEDOUTPUTPREPEND);

        logger.info("Reading data from: " + config.ctScanDataFile);


        filesystemConnectivityChecker.check();
        databaseConnectivityChecker.check();



        if (!preliminaryChecksPassed()) {
            logger.error("Preliminary checks failed, aborting operation...");
            System.exit(1);
        }

        List scansList = sourceReader.readScans(config.ctScanDataFile);


        Iterator<CtScan> ctScanIterator = scansList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            logger.info("Working on scan: " + ctScan.getFolderLocation());
            ctScanService.preprocessScanFolderLocation(ctScan);
            ctScanService.updateDicomFolder(ctScan);
            ctScanService.updateTimestamp(ctScan);


        }

        //TODO: Refactor validations
        try {
            ctScanValidatorService.validateScanData(scansList);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.toString());
            systemExit.exit(1);
        }
        ctScanDataChecker.check(scansList);

        // TODO: Add a new class for deciding whether to continue or not. (remove this decision from the validator)


        ctScanUtilsService.findStandardizedFolderNames(scansList);
        //ctScanValidatorService.validateStandardizedFolderNames(scansList, failedValidationOutput);
        //ctScanValidatorService.validateUniquenessOfFolders(scansList, failedValidationOutput);
       // fileUtils.writeBeansToFile(scansList, outputFile);
        //ctScanMigratorService.migrateScans(scansList, outputFile, DUMMY_EXECUTION);


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
       // return !(dbUtil.specimenCodeExists("TEST"));
        return false;
    }


}


