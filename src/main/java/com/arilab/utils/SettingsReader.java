package com.arilab.utils;

import com.arilab.domain.CTScanValidator;
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



    public String prependBucketString;

    private  Logger logger = LoggerFactory.getLogger(CTScanValidator.class);



    public SettingsReader() {
        readCredentials();
        readDatabaseHost();
        readBucketPrependString();
    }


    public void readCredentials() {
        try {
            File usernameFile = new File("/opt/CTScanMigrator/username.pass");
            BufferedReader br = new BufferedReader(new FileReader(usernameFile));
            credentials.add(br.readLine());
            credentials.add(br.readLine());
        } catch (IOException exc) {
            logger.error("Exception caught:" + exc.toString());
        }
    }


    public void readDatabaseHost () {
        try {
            File dbHostFile = new File("/opt/CTScanMigrator/dbhost.settings");
            BufferedReader br = new BufferedReader(new FileReader(dbHostFile));
            dbHost = br.readLine();
        } catch (IOException exc) {
            logger.error("Exception caught: " + exc.toString());
        }

    }

    public void readBucketPrependString() {
        try {
            File usernameFile = new File("/opt/CTScanMigrator/other.settings");
            BufferedReader br = new BufferedReader(new FileReader(usernameFile));
            prependBucketString = br.readLine();

        } catch (IOException exception) {
            logger.error("Exception caught: " + exception);
        }

    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getPrependBucketString() {
        return prependBucketString;
    }

    public void setPrependBucketString(String prependBucketString) {
        this.prependBucketString = prependBucketString;
    }



}
