package com.arilab;

import com.arilab.domain.CTScanValidator;
import com.arilab.utils.CTScanMigrator;
import com.arilab.utils.CTScanUtils;
import com.arilab.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {

    private static final String FILE_TO_WRITE = "./BeanOutput.csv";
    private static final String CTSCAN_DATA_FILE = "/home/kosmas-deligkaris/repositories/arilabdb" +
            "/202106_Add_Congs_data/SourcesFromPaco" +
            "/CTScansForUpload.csv";

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static CTScanValidator validator = new CTScanValidator();
    private static FileUtils fileUtils = new FileUtils();
    private static CTScanUtils ctScanUtils = new CTScanUtils();
    private static CTScanMigrator ctScanMigrator = new CTScanMigrator();

    public static void main(String[] args) {
        int validScans;

        List scansList = fileUtils.getScansFromFile(CTSCAN_DATA_FILE);
        validScans = prepareScans(scansList);

        if (validScans != scansList.size()) {
            logger.error("Not all scans passed validation, migration will not proceed.");
            System.exit(1);
        }

        ctScanMigrator.migrateScans(scansList);


    }

    private static int prepareScans(List scansList) {
        int validScans;
        ctScanUtils.fixScans(scansList); // standardizes folder locations, among others.
        validScans = validator.validate(scansList); // validates if scans can be migrated, all info is complete and
        // correct.
        logger.info("Finished preliminary checks and modifications, " + validScans + " valid scans were found, out of" +
                            " " + scansList.size());
        fileUtils.writeBeansToFile(scansList, FILE_TO_WRITE);
        return validScans;
    }


}


