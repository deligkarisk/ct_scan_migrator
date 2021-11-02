package com.arilab;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.domain.CtScanValidator;
import com.arilab.flowcontroller.*;
import com.arilab.reader.SourceReader;
import com.arilab.repository.CtScanRepository;
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

    private static final StringUtils stringUtils = new StringUtils();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final FileUtils fileUtils = new FileUtils();
    private static Config config = Config.createInstance(PROPERTIES_FILE, CREDENTIALS_FILE);
    public static final SourceReader sourceReader = new SourceReader();
    public static final ArgumentChecker argumentChecker = new ArgumentChecker();
    public static final SystemExit systemExit = new SystemExit();
    private static final PathUtils pathUtils = new PathUtils(config, systemExit);
    private static final CtScanRepository ctScanRepository = new CtScanRepository(config);

    public static final FileSystemUtils filesystemUtils = new FileSystemUtils();
    private static final DatabaseRepository DATABASE_REPOSITORY = new DatabaseRepository(systemExit, config);
    private static final DatabaseService databaseService = new DatabaseService(DATABASE_REPOSITORY, systemExit);
    private static final CtScanValidator ctScanValidator = new CtScanValidator(databaseService, pathUtils);
    private static final CtScanCollectionValidator ctScanCollectionValidator = new CtScanCollectionValidator();
    private static final CtScanUtils ctScanUtils = new CtScanUtils(databaseService, pathUtils, config);
    private static final CTScanService ctScanService = new CTScanService(pathUtils, ctScanUtils, ctScanRepository,
            ctScanValidator, databaseService, config);
    public static final FilesystemConnectivityChecker filesystemConnectivityChecker =
            new FilesystemConnectivityChecker(config, systemExit, filesystemUtils);
    public static final CtScanDataChecker ctScanDataChecker = new CtScanDataChecker(fileUtils, config, systemExit);
    public static final DatabaseConnectivityChecker databaseConnectivityChecker =
            new DatabaseConnectivityChecker(databaseService, config, systemExit);
    public static final StandardizedFoldersChecker standardizedFoldersChecker = new StandardizedFoldersChecker(config
            , systemExit, fileUtils);

    private static final CtScanMigrator ctScanMigrator = new CtScanMigrator(fileUtils, DATABASE_REPOSITORY, ctScanRepository);
    private static final CTScanMigratorService ctScanMigratorService = new CTScanMigratorService(fileUtils,
            ctScanMigrator, config);
    private static final CtScanCollectionService ctScanCollectionService = new CtScanCollectionService(ctScanService);


    public static void main(String[] args)  {

        argumentChecker.check(args);

        if ((args.length == 3) && (args[2].equals("--do-migration"))) {
            DUMMY_EXECUTION = FALSE;
        }

        String ctScanDataFile = args[0];
        String dataLabel = args[1];

        String outputFile = stringUtils.mergeStrings(OUTPUT_PREPEND, dataLabel);
        String failedOutputFile = stringUtils.mergeStrings(FAILEDOUTPUTPREPEND, dataLabel);

        logger.info("************************** Starting app **************************");


        //config =  new Config(PROPERTIES_FILE, CREDENTIALS_FILE,args[0], args[1], OUTPUT_PREPEND, FAILEDOUTPUTPREPEND);


        filesystemConnectivityChecker.check();
        databaseConnectivityChecker.check();


        logger.info("Reading data from: " + ctScanDataFile);

        CtScanCollection ctScanCollection = new CtScanCollection(sourceReader.readScans(ctScanDataFile));

        try {
            ctScanCollectionService.preprocessData(ctScanCollection);
            ctScanCollectionService.validateScanData(ctScanCollection);
            ctScanDataChecker.check(ctScanCollection); // Decides whether to continue or not
        } catch (SQLException sqlException) {
            logger.error("Exception caught during migration: {}", sqlException.toString());
            fileUtils.writeBeansToFile(ctScanCollection.getCtScans(), outputFile);
            systemExit.exit(1);
        }
        ctScanCollectionService.findStandardizedFolderNames(ctScanCollection);


        try {
           validateStandardizedFolderNames(scansList);
        } catch (SQLException e) {
            e.printStackTrace();
            // todo: quit application
        }
        standardizedFoldersChecker.check(scansList); // Decides whether to continue or not


       boolean allFoldersUnique = ctScanCollectionValidator.areAllFoldersUniqueInCollection(scansList);

       if (!allFoldersUnique) {
            logger.error("Not all folders unique, system will exit.");
            systemExit.exit(1);
        }

        fileUtils.writeBeansToFile(scansList, outputFile);


        try {
            ctScanMigratorService.migrateScans(scansList, outputFile, DUMMY_EXECUTION);
        } catch (SQLException|IOException exception) {
            logger.error("Exception caught during migration: {}", exception.toString());
            fileUtils.writeBeansToFile(scansList, outputFile);
            systemExit.exit(1);
        }


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
        Boolean oldBucketFolderIsMounted = Files.exists(Paths.get(config.getSourceDirectory()));
        Boolean newBucketFolderIsMounted = Files.exists(Paths.get(config.getTargetDirectory()));
        return ((oldBucketFolderIsMounted && newBucketFolderIsMounted));
    }

    private static Boolean canWriteToBucket() throws IOException {
        Files.deleteIfExists(Paths.get(config.getTargetDirectory(), "testFile"));
        Files.createFile(Paths.get(config.getTargetDirectory(), "testFile"));
        Files.deleteIfExists(Paths.get(config.getTargetDirectory(), "testFile"));
        return true;
    }

    private static Boolean checkDBConnectivity() {
        // return !(dbUtil.specimenCodeExists("TEST"));
        return false;
    }






    private static void validateStandardizedFolderNames(List<CtScan> ctScanList) throws SQLException {
        Iterator<CtScan> ctScanIterator = ctScanList.iterator();
        while (ctScanIterator.hasNext()) {
            CtScan ctScan = ctScanIterator.next();
            ctScan.validateStandardizedFolder(pathUtils, ctScanService);
        }
    }
}


