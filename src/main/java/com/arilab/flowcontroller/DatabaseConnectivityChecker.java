package com.arilab.flowcontroller;

import com.arilab.service.DatabaseService;
import com.arilab.system.SystemExit;
import com.arilab.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DatabaseConnectivityChecker {

    DatabaseService databaseService;
    SystemExit systemExit;

    private static Logger logger = LoggerFactory.getLogger(DatabaseConnectivityChecker.class);

    public DatabaseConnectivityChecker(DatabaseService databaseService, SystemExit systemExit) {
        this.databaseService = databaseService;
        this.systemExit = systemExit;
    }

    public void check() {
        try {
            databaseService.specimenCodeExists("Test");
        } catch (SQLException e) {
            logger.error("Error in connectind to the database: " + e.getMessage() );
            systemExit.exit(1);
        }
    }
}
