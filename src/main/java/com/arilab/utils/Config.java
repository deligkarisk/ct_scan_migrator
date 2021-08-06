package com.arilab.utils;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Config {

    private static Config configInstance;
    private static String PROPERTIES_FILE = "./config.properties";
    private static String CREDENTIALS_FILE = "./credentials.properties";
    private static Logger logger = LoggerFactory.getLogger(Config.class);

    public String dbhost;
    public String sourceDirectory;
    public String targetDirectory;
    public int dicomLevelsUp;
    public String dicomAppendString;



    public String username;
    public String password;




    private Config() {
        Configurations configurations = new Configurations();
        try {
            Configuration configuration =  configurations.properties(new File(PROPERTIES_FILE));
            dbhost = configuration.getString("database.host");
            sourceDirectory = configuration.getString("source.directory");
            targetDirectory = configuration.getString("target.directory");
            dicomLevelsUp = configuration.getInt("dicom.levelsup");
            dicomAppendString = configuration.getString("dicom.append.string");

            Configuration configurationCredentials = configurations.properties(new File(CREDENTIALS_FILE));
            username = configurationCredentials.getString("username");
            password = configurationCredentials.getString("password");



        } catch (ConfigurationException e) {
            logger.error("Could not load configuration settings, aborting operation.");
            System.exit(1);
        }



    }

    public static Config getInstance() {
        if (configInstance == null) {
            configInstance = new Config();
        }
        return configInstance;
    }
}
