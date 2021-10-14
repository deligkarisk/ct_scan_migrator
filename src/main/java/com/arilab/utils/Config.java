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

    private String dbhost;
    private String sourceDirectory;
    private String targetDirectory;
    private int dicomLevelsUp;
    private String dicomAppendString;

    private String username;
    private String password;

    private String failedOutputFile;



    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Config.logger = logger;
    }

    public String getDbhost() {
        return dbhost;
    }

    public void setDbhost(String dbhost) {
        this.dbhost = dbhost;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public int getDicomLevelsUp() {
        return dicomLevelsUp;
    }

    public void setDicomLevelsUp(int dicomLevelsUp) {
        this.dicomLevelsUp = dicomLevelsUp;
    }

    public String getDicomAppendString() {
        return dicomAppendString;
    }

    public void setDicomAppendString(String dicomAppendString) {
        this.dicomAppendString = dicomAppendString;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFailedOutputFile() {
        return failedOutputFile;
    }

    public void setFailedOutputFile(String failedOutputFile) {
        this.failedOutputFile = failedOutputFile;
    }

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


}
