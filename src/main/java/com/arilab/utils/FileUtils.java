package com.arilab.utils;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
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
    DirectoryMover directoryMover;

    public FileUtils(DirectoryMover directoryMover) {
        this.directoryMover = directoryMover;
    }

    public Boolean folderExists(Path folder) {
        return Files.exists(folder);
    }

    public void moveMainFolder(CtScan ctScan, Boolean dummyMigrationFlag) throws IOException {
        // todo maybe these should go into the ctscan migrator service, and have a simple moveFolder method here.
        File srcDir = new File(ctScan.getFolderLocation());
        File destinationDir = new File(ctScan.getNewFolderPath());
        logger.info("Moving folder " + srcDir.toString() + " to " + destinationDir.toString());
        if (!dummyMigrationFlag) {
            directoryMover.move(srcDir, destinationDir);
        }
        logger.info("Moving of raw/main data folder completed");
    }

    public void moveDicomFolder(CtScan ctScan, Boolean dummyMigrationFlag) throws IOException {
        // todo maybe these should go into the ctscan migrator service, and have a simple moveFolder method here.
        File dicomSrcDir = new File(ctScan.getDicomFolderLocation());
        File dicomDestinationDir = new File(ctScan.getNewDicomFolderPath());
        logger.info("Moving dicom folder " + dicomSrcDir.toString() + " to " + dicomDestinationDir.toString());
        if (!dummyMigrationFlag) {
            directoryMover.move(dicomSrcDir, dicomDestinationDir);
        }
        logger.info("Moving of the dicom folder completed");
    }

    public void writeBeansToFile(CtScanCollection ctScanCollection, String filePath) {
        List<CtScan> scanList = ctScanCollection.getCtScans();

        try {
            Writer writer = new FileWriter(filePath);
            StatefulBeanToCsv statefulBeanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            statefulBeanToCsv.write(scanList);
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





}
