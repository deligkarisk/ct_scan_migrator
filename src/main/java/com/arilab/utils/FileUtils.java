package com.arilab.utils;

import com.arilab.domain.CtScan;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileUtils {

    private  Logger logger = LoggerFactory.getLogger(FileUtils.class);




    public void writeBeansToFile(List<CtScan> ctScans, String filePath) {
        try {
            Writer writer = new FileWriter(filePath);
            StatefulBeanToCsv statefulBeanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            statefulBeanToCsv.write(ctScans);
            writer.close();
        } catch (IOException ioException) {
            logger.error("IO Exception in opening the file to be written." + ioException.toString());
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
            logger.error("CSV Required field empty exception" + e.toString());
        } catch (CsvDataTypeMismatchException e) {
            logger.error("CSV data type mismatch exception" + e.toString());
            e.printStackTrace();
        }
    }

    public void migrateFolder(CtScan ctScan, Boolean dummyMigrationFlag) throws IOException {
        File srcDir = new File(ctScan.getFolderLocation());
        File destinationDir = new File(ctScan.getNewFolderPath());
        logger.info("Moving folder " + srcDir.toString() + " to " + destinationDir.toString());
        if (!dummyMigrationFlag) {
            org.apache.commons.io.FileUtils.moveDirectory(srcDir, destinationDir);
        }
        logger.info("Moving of raw/main data folder completed");

        if (ctScan.getDicomFolderLocation() != null) {
            File dicomSrcDir = new File(ctScan.getDicomFolderLocation());
            String label = Paths.get(ctScan.getNewFolderPath()).getFileName().toString();
            String dicomFileLabel = label + "_dicom";

            File dicomDestinationDir = new File(ctScan.getNewFolderPath(), dicomFileLabel);
            if (dicomDestinationDir.exists()) {
                logger.error("Dicom folder already exists in the destination folder. Adding 2 to the dicome folder name....");
                dicomDestinationDir = new File(ctScan.getNewFolderPath(), "Dicom2");
            }
            logger.info("Moving dicom folder " + dicomSrcDir.toString() + " to " + dicomDestinationDir.toString());
            if (!dummyMigrationFlag) {
                org.apache.commons.io.FileUtils.moveDirectory(dicomSrcDir, dicomDestinationDir);
            }
            logger.info("Moving of the dicom folder completed");
        }
    }
}
