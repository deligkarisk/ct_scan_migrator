package com.arilab;

import com.arilab.domain.CtScanCollection;
import com.arilab.domain.validator.CtScanCollectionValidator;
import com.arilab.domain.validator.CtScanValidator;
import com.arilab.domain.validator.error.ErrorModel;
import com.arilab.domain.validator.group.AllFoldersUniqueValidationGroup;
import com.arilab.domain.validator.group.BasicFieldValidationGroup;
import com.arilab.domain.validator.group.StandardizedFoldersValidationGroup;
import com.arilab.domain.validator.group.ValidatorGroup;
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
import java.util.List;

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

    public static final FileSystem filesystem = new FileSystem(config);
    private static final DatabaseRepository DATABASE_REPOSITORY = new DatabaseRepository(config);
    private static final DatabaseService databaseService = new DatabaseService(DATABASE_REPOSITORY, systemExit);
    private static final CtScanValidationService ctScanValidationService = new CtScanValidationService();
    private static final CtScanUtils ctScanUtils = new CtScanUtils(databaseService, config);
    private static final CTScanService ctScanService = new CTScanService(fileUtils, ctScanUtils, ctScanRepository,
            databaseService, config);
    public static final UniqueFoldersChecker uniqueFoldersChecker = new UniqueFoldersChecker();
    private static final CTScanMigratorService ctScanMigratorService = new CTScanMigratorService(fileUtils,
            ctScanRepository);

    private static final CtScanCollectionValidationService ctScanCollectionValidationService =
            new CtScanCollectionValidationService();

    private static final CtScanCollectionService ctScanCollectionService = new CtScanCollectionService(ctScanService, uniqueFoldersChecker, ctScanValidationService, ctScanCollectionValidationService);


    private static final ValidatorGroup<CtScanValidator> basicFieldValidationGroup =
            new BasicFieldValidationGroup(databaseService);

    private static final ValidatorGroup<CtScanValidator> standardizedFoldersValidationGroup =
            new StandardizedFoldersValidationGroup(ctScanService);

    private static final ValidatorGroup<CtScanCollectionValidator> allFoldersUniqueValidationGroup =
            new AllFoldersUniqueValidationGroup();

    static List<ErrorModel> errors = new ArrayList<>();


    public static void main(String[] args) {

        CtScanCollection ctScanCollection = null;

        argumentChecker.check(args);

        if ((args.length == 3) && (args[2].equals("--do-migration"))) {
            DUMMY_EXECUTION = FALSE;
        }

        String ctScanDataFile = args[0];
        String dataLabel = args[1];

        String outputFile = mergeStrings(OUTPUT_PREPEND, dataLabel);
        String failedOutputFile = mergeStrings(FAILEDOUTPUTPREPEND, dataLabel);

        logger.info("************************** Starting app **************************");


        try {
            filesystem.filesystemCheck();
            databaseService.checkDatabaseConnectivity();
            logger.info("Reading data from: " + ctScanDataFile);
            ctScanCollection = new CtScanCollection(sourceReader.readScans(ctScanDataFile));
            ctScanCollectionService.preprocessData(ctScanCollection);
            errors = ctScanCollectionService.validateCollectionAtScanLevel(basicFieldValidationGroup,
                    ctScanCollection); //todo: write test that checks that all validators run
            quitIfErrors(errors, failedOutputFile, ctScanCollection);
            ctScanCollectionService.findStandardizedFolderNames(ctScanCollection);
            errors = ctScanCollectionService.validateCollectionAtScanLevel(standardizedFoldersValidationGroup,
                    ctScanCollection); //todo: write test that checks that all validators run
            quitIfErrors(errors, failedOutputFile, ctScanCollection);
            // ctScanCollectionService.validateAllFoldersUniqueInCollection(ctScanCollection);
            errors = ctScanCollectionService.validateCollection(allFoldersUniqueValidationGroup, ctScanCollection);
            quitIfErrors(errors, failedOutputFile, ctScanCollection);
            // Before migrating, write all information to the output file.
            fileUtils.writeBeansToFile(ctScanCollection, outputFile);


        } catch (SQLException | IOException Exception) {
            logger.error("Exception caught during migration: {}", Exception.toString());
            if (ctScanCollection == null) {
                fileUtils.writeBeansToFile(ctScanCollection, outputFile);
            }
            systemExit.exit(1);
        }


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

    private static void quitIfErrors(List<ErrorModel> error, String failedOutputFile,
                                     CtScanCollection ctScanCollection) {
        if (!error.isEmpty()) {
            logger.error("Not all scans passed validation of input data, migration will not proceed. Please see the" +
                    " file " + failedOutputFile + " for further details.");
            fileUtils.writeBeansToFile(ctScanCollection, failedOutputFile);
            systemExit.exit(1);
        }
    }

    private static void checkConnectivityToBucketAndDatabase() {
        try {
            databaseService.specimenCodeExists("Test");
        } catch (SQLException e) {
            logger.error("Error in connecting to the database: " + e.getMessage());
            throw new RuntimeException("Error in connecting to the database.");
        }
    }
}