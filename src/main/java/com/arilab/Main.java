package com.arilab;

import com.arilab.domain.CtScanCollection;
import com.arilab.domain.validator.CtScanCollectionValidator;
import com.arilab.domain.validator.CtScanValidator;
import com.arilab.domain.validator.error.ErrorModel;
import com.arilab.domain.validator.group.AllFoldersUniqueValidationGroup;
import com.arilab.domain.validator.group.BasicFieldValidationGroup;
import com.arilab.domain.validator.group.StandardizedFoldersValidationGroup;
import com.arilab.domain.validator.group.ValidatorGroup;
import com.arilab.reader.SourceReader;
import com.arilab.repository.CtScanRepository;
import com.arilab.repository.DatabaseRepository;
import com.arilab.service.*;
import com.arilab.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
    private static final CtScanRepository ctScanRepository = new CtScanRepository(config);

    public static final FileSystemService filesystemService = new FileSystemService(config);
    private static final DatabaseRepository DATABASE_REPOSITORY = new DatabaseRepository(config);
    private static final DatabaseService databaseService = new DatabaseService(DATABASE_REPOSITORY);
    private static final CtScanUtils ctScanUtils = new CtScanUtils(databaseService, config);
    private static final CTScanService ctScanService = new CTScanService(fileUtils, ctScanUtils, ctScanRepository,
            databaseService, config);
    private static final CTScanMigratorService ctScanMigratorService = new CTScanMigratorService(fileUtils,
            ctScanRepository);
    private static final CtScanCollectionService ctScanCollectionService = new CtScanCollectionService(ctScanService);

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

            // Check filesystem and database connectivity first
            filesystemService.filesystemCheck();
            databaseService.databaseConnectivityCheck();
            logger.info("Reading data from: " + ctScanDataFile);

            // preprocess data for use, and validate entries
            ctScanCollection = new CtScanCollection(sourceReader.readScans(ctScanDataFile));
            ctScanCollectionService.preprocessData(ctScanCollection);
            errors = ctScanCollectionService.validateCollectionAtScanLevel(basicFieldValidationGroup,
                    ctScanCollection);
            quitIfErrors(errors, failedOutputFile, ctScanCollection);

            // find and validate the new folders for the scans (based on the lab's standardized format)
            ctScanCollectionService.findStandardizedFolderNames(ctScanCollection);
            errors = ctScanCollectionService.validateCollectionAtScanLevel(standardizedFoldersValidationGroup,
                    ctScanCollection);
            quitIfErrors(errors, failedOutputFile, ctScanCollection);
            errors = ctScanCollectionService.validateCollection(allFoldersUniqueValidationGroup, ctScanCollection);
            quitIfErrors(errors, failedOutputFile, ctScanCollection);


            // Before actually doing the migration, write all information to the output file.
            fileUtils.writeBeansToFile(ctScanCollection, outputFile);
            ctScanMigratorService.migrateScans(ctScanCollection, DUMMY_EXECUTION);

        } catch (SQLException | IOException Exception) {
            logger.error("Exception caught during migration: {}", Exception.toString());
            if (ctScanCollection != null) {
                fileUtils.writeBeansToFile(ctScanCollection, outputFile);
            }
            System.exit(1);
        }


        logger.info("************************** Finished Execution **************************");
    }


    private static String mergeStrings(String prepend, String label) {
        return ("./" + prepend + "_" + label + ".csv");
    }

    private static void quitIfErrors(List<ErrorModel> error, String failedOutputFile,
                                     CtScanCollection ctScanCollection) throws FileNotFoundException, UnsupportedEncodingException {
        // todo: when quiting due to errors, the output of the validators should be exported to a file.
        if (!error.isEmpty()) {
            logger.error("Not all scans passed validation of input data, migration will not proceed. Please see the" +
                    " file " + failedOutputFile + " for further details.");
            fileUtils.writeBeansToFile(ctScanCollection, failedOutputFile);
            PrintWriter errorFileWriter = new PrintWriter("errors.log", "UTF-8");
            for (ErrorModel errorModel : error) {
                errorFileWriter.println(errorModel.toString());
                List<String> errors = errorModel.getErrors();
                for (String singleError : errors) {
                    errorFileWriter.println(singleError);
                }
                errorFileWriter.println();
            }
            errorFileWriter.close();
            System.exit(1);
        }
    }
}