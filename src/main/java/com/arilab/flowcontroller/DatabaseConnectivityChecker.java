package com.arilab.flowcontroller;

import com.arilab.service.DatabaseService;
import com.arilab.system.SystemExit;
import com.arilab.utils.Config;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class DatabaseConnectivityChecker {

    DatabaseService databaseService;
    Config config;
    SystemExit systemExit;

    public DatabaseConnectivityChecker(DatabaseService databaseService, Config config, SystemExit systemExit) {
        this.databaseService = databaseService;
        this.config = config;
        this.systemExit = systemExit;
    }

    public void check() {
        try {
            databaseService.specimenCodeExists("Test");
        } catch (SQLException e) {
            e.printStackTrace();
            systemExit.exit(1);
        }
    }
}
