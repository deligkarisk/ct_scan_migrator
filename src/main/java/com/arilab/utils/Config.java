package com.arilab.utils;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Config {

    private static Config configInstance;

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    public String dbhost;
    public String sourceDirectory;
    public String targetDirectory;
    public int dicomLevelsUp;
    public String dicomAppendString;

    public String username;
    public String password;

    public String outputFile;
    public String failedOutputFile;
    //public String ctScanDataFile;




    private Config(String propertiesFile, String credentialsFile) {
        Configurations configurations = new Configurations();
        try {
            Configuration configuration =  configurations.properties(new File(propertiesFile));
            dbhost = configuration.getString("database.host");
            sourceDirectory = configuration.getString("source.directory");
            targetDirectory = configuration.getString("target.directory");
            dicomLevelsUp = configuration.getInt("dicom.levelsup");
            dicomAppendString = configuration.getString("dicom.append.string");

            Configuration configurationCredentials = configurations.properties(new File(credentialsFile));
            username = configurationCredentials.getString("username");
            password = configurationCredentials.getString("password");

           // this.ctScanDataFile = ctScanDataFile;

        } catch (ConfigurationException e) {
            logger.error("Could not load configuration settings, aborting operation.");
            System.exit(1);
        }

    }



    public static Config createInstance(String propertiesFile, String credentialsFile) {
        if (configInstance != null) {
            throw new AssertionError("Config singleton already initialized");
        }

        configInstance = new Config(propertiesFile, credentialsFile);
        return configInstance;
    }


    public static Config getInstance() {
        if (configInstance == null) {
           throw new AssertionError("Singleton has not been initialized yet, nothing to retrieve");
        }
        return configInstance;
    }
}
