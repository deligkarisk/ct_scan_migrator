package com.arilab.utils;

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

    public PathUtils(Config config) {
        this.config = config;
    }

    public Boolean folderExists(Path folder) {
        return Files.exists(folder);
    }

    public Path getCorrectScanFolderLocation(String currentLocation) {
        String newLocation = currentLocation.split("CT_Scan_2017-2019")[1];
        Path newLocationPath = Paths.get(config.sourceDirectory, newLocation);
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

