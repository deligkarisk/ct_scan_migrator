package com.arilab;

import com.arilab.domain.CtScanCollection;
import com.arilab.domain.CtScanCollectionValidator;
import com.arilab.domain.CtScanValidator;
import com.arilab.domain.validator.ValidationGroup1;
import com.arilab.domain.validator.ValidatorGroup;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Boolean.FALSE;

public class Main {

    private static Boolean DUMMY_EXECUTION = true;
    private static final String PROPERTIES_FILE = "./config.properties";
    private static final String CREDENTIALS_FILE = "./credentials.properties";
    private static final String OUTPUT_PREPEND = "MigrationOutput";
    private static final String FAILEDOUTPUTPREPEND = "ValidationFailed";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final DirectoryMover properDirectoryMover = new DirectoryMoverProper();
    private static final FileUtils fileUtils = new FileUtils(properDirectoryMover);
    private static Config config = Config.createInstance(PROPERTIES_FILE, CREDENTIALS_FILE);
    public static final SourceReader sourceReader = new SourceReader();
    public static final ArgumentChecker argumentChecker = new ArgumentChecker();
    public static final SystemExit systemExit = new SystemExit();
    private static final CtScanRepository ctScanRepository = new CtScanRepository(config);

    public static final FileSystemUtils filesystemUtils = new FileSystemUtils();
    private static final DatabaseRepository DATABASE_REPOSITORY = new DatabaseRepository(config);
    private static final DatabaseService databaseService = new DatabaseService(DATABASE_REPOSITORY, systemExit);
    private static final CtScanValidator ctScanValidator = new CtScanValidator(databaseService, fileUtils);
    private static final CtScanValidationService ctScanValidationService = new CtScanValidationService();
    private static final CtScanCollectionValidator ctScanCollectionValidator = new CtScanCollectionValidator();
    private static final CtScanUtils ctScanUtils = new CtScanUtils(databaseService, config);
    private static final CTScanService ctScanService = new CTScanService(fileUtils, ctScanUtils, ctScanRepository,
            ctScanValidator, databaseService, config);
    public static final FilesystemConnectivityChecker filesystemConnectivityChecker =
            new FilesystemConnectivityChecker(config, systemExit, filesystemUtils);
    public static final CtScanDataChecker ctScanDataChecker = new CtScanDataChecker(fileUtils, systemExit);
    public static final DatabaseConnectivityChecker databaseConnectivityChecker =
            new DatabaseConnectivityChecker(databaseService, systemExit);
    public static final StandardizedFoldersChecker standardizedFoldersChecker = new StandardizedFoldersChecker(systemExit, fileUtils);
    public static final UniqueFoldersChecker uniqueFoldersChecker = new UniqueFoldersChecker();

    private static final CTScanMigratorService ctScanMigratorService = new CTScanMigratorService(fileUtils,
            ctScanRepository);
    private static final CtScanCollectionService ctScanCollectionService = new CtScanCollectionService(ctScanService,
            ctScanCollectionValidator, uniqueFoldersChecker, ctScanValidationService);


    private static final ValidatorGroup validatorGroup1 = new ValidationGroup1();


    public static void main(String[] args) {

        argumentChecker.check(args);

        if ((args.length == 3) && (args[2].equals("--do-migration"))) {
            DUMMY_EXECUTION = FALSE;
        }

        String ctScanDataFile = args[0];
        String dataLabel = args[1];

        String outputFile = mergeStrings(OUTPUT_PREPEND, dataLabel);
        String failedOutputFile = mergeStrings(FAILEDOUTPUTPREPEND, dataLabel);

        logger.info("************************** Starting app **************************");


        filesystemConnectivityChecker.check();
        databaseConnectivityChecker.check();


        logger.info("Reading data from: " + ctScanDataFile);

        CtScanCollection ctScanCollection = new CtScanCollection(sourceReader.readScans(ctScanDataFile));

        try {
            ctScanCollectionService.preprocessData(ctScanCollection);
            HashMap<String, ArrayList<String>> errors = ctScanCollectionService.validateCollection(validatorGroup1,
                    ctScanCollection);
           // ctScanCollectionService.validateScanData(ctScanCollection);
            ctScanDataChecker.check(ctScanCollection, failedOutputFile); // Decides whether to continue or not
        } catch (SQLException sqlException) {
            logger.error("Exception caught during migration: {}", sqlException.toString());
            fileUtils.writeBeansToFile(ctScanCollection, outputFile);
            systemExit.exit(1);
        }
        ctScanCollectionService.findStandardizedFolderNames(ctScanCollection);


        try {
            ctScanCollectionService.validateStandardizedFolderNames(ctScanCollection);
        } catch (SQLException e) {
            logger.error("SQL exception when validating the new standardized folders, exiting.");
            systemExit.exit(1);
        }

        standardizedFoldersChecker.check(ctScanCollection, failedOutputFile); // Decides whether to continue or not
        ctScanCollectionService.validateAllFoldersUniqueInCollection(ctScanCollection);

        // Before migrating, write all information to the output file.
        fileUtils.writeBeansToFile(ctScanCollection, outputFile);


        try {
            ctScanMigratorService.migrateScans(ctScanCollection, DUMMY_EXECUTION);
        } catch (SQLException | IOException exception) {
            logger.error("Exception caught during migration: {}", exception.toString());
            fileUtils.writeBeansToFile(ctScanCollection, outputFile);
            systemExit.exit(1);
        }


        logger.info("************************** Finished Execution **************************");
    }


    private static String mergeStrings(String prepend, String label) {
        return ("./" + prepend + "_" + label + ".csv");
    }

}