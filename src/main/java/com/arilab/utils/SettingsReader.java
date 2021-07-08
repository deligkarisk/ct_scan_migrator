package com.arilab.utils;

import com.arilab.domain.CtScanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SettingsReader {

    public List<String> credentials = new LinkedList<>();
    public String dbHost;
    public String prependBucketStringOld;
    public String prependBucketStringNew;


    private Logger logger = LoggerFactory.getLogger(CtScanValidator.class);

    private String CREDENTIALS_FILE = "./username.pass";
    private String DATABASE_HOST_FILE = "./dbhost.settings";
    private String OTHER_SETTINGS_FILE = "./other.settings";


    public SettingsReader() {
        readCredentials(CREDENTIALS_FILE);
        readDatabaseHost(DATABASE_HOST_FILE);
        readBucketPrependString(OTHER_SETTINGS_FILE);
    }


    public void readCredentials(String credentialsFile) {
        try {
            File usernameFile = new File(credentialsFile);
            BufferedReader br = new BufferedReader(new FileReader(usernameFile));
            credentials.add(br.readLine());
            credentials.add(br.readLine());
        } catch (IOException exc) {
            logger.error("Exception caught:" + exc.toString());
            System.exit(1);
        }
    }


    public void readDatabaseHost(String databaseHostFile) {
        try {
            File dbHostFile = new File(databaseHostFile);
            BufferedReader br = new BufferedReader(new FileReader(dbHostFile));
            dbHost = br.readLine();
        } catch (IOException exc) {
            logger.error("Exception caught: " + exc.toString());
            System.exit(1);
        }

    }

    public void readBucketPrependString(String otherSettingsFile) {
        try {
            File usernameFile = new File(otherSettingsFile);
            BufferedReader br = new BufferedReader(new FileReader(usernameFile));
            prependBucketStringOld = br.readLine();
            prependBucketStringNew = br.readLine();

        } catch (IOException exception) {
            logger.error("Exception caught: " + exception);
            System.exit(1);
        }

    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getPrependBucketStringOld() {
        return prependBucketStringOld;
    }

    public void setPrependBucketStringOld(String prependBucketStringOld) {
        this.prependBucketStringOld = prependBucketStringOld;
    }


    public String getPrependBucketStringNew() {
        return prependBucketStringNew;
    }

    public void setPrependBucketStringNew(String prependBucketStringNew) {
        this.prependBucketStringNew = prependBucketStringNew;
    }

}
