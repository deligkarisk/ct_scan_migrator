package com.arilab.utils;

import com.arilab.system.SystemExit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathUtils {

    private static Logger logger = LoggerFactory.getLogger(PathUtils.class);
    private Config config;
    private SystemExit systemExit;

    public PathUtils(Config config, SystemExit systemExit) {
        this.config = config;
        this.systemExit = systemExit;
    }

    public Boolean folderExists(Path folder) {
        return Files.exists(folder);
    }



        public Path fixPrependPath(String currentLocation) {
        String parentFolderToSplit = Paths.get(config.getSourceDirectory()).getFileName().toString();
        String[] splitResult = currentLocation.split(parentFolderToSplit);

        if (splitResult.length < 2) {
            logger.error("The pattern " + parentFolderToSplit + " was not found in the current location, please check" +
                    " your inputs and try again. Location used: " + currentLocation);
            systemExit.exit(1);
            return null; //to ensure subsequent code does not run, during tests
        }


        String newLocation = splitResult[1];
        Path newLocationPath = Paths.get(config.getSourceDirectory(), newLocation);
        return newLocationPath;

    }


    public String extractTimestamp(String folderPath) {
        String returnString = null;
        Pattern pattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d_\\d\\d\\d\\d\\d\\d");
        Matcher matcher = pattern.matcher(folderPath);
        if (matcher.find()) {
            returnString = matcher.group(0);
        }
        return returnString;
    }

    public String generateDicomPotentialFolder(String folderLocation, int levelsBack, String appendString) {
        Path dicomFolderPath = Paths.get(folderLocation);
        for (int i = 0; i < levelsBack; i++) {
            dicomFolderPath = dicomFolderPath.getParent();
        }
        String dicomStringPath = Paths.get(dicomFolderPath.getParent().toString(),
                dicomFolderPath.getFileName().toString() + appendString).toString();
        return dicomStringPath;
    }
}

