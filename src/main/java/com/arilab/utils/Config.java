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
    private static Logger logger = LoggerFactory.getLogger(Config.class);

    public String dbhost;


    private Config() {
        Configurations configurations = new Configurations();
        try {
            Configuration configuration =  configurations.properties(new File(PROPERTIES_FILE));
            dbhost = configuration.getString("database.host");
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
