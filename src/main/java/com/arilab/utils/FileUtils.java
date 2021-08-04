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
import java.util.List;

public class FileUtils {

    private  Logger logger = LoggerFactory.getLogger(FileUtils.class);


    public  List getScansFromFile(String file) {
        try {
            Reader fileReader = Files.newBufferedReader(Path.of(file));
            List csvToBean = new CsvToBeanBuilder(fileReader).withType(CtScan.class).withIgnoreLeadingWhiteSpace(
                    true).withFieldAsNull(CSVReaderNullFieldIndicator.BOTH).build().parse();
            logger.info("Found " + csvToBean.size() + " scans");
            return csvToBean;
        } catch (IOException exception) {
            logger.error("Could not find data file " + exception.toString());
        }
        return null;
    }


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

    public void migrateFolder(CtScan ctScan) throws IOException {
        File srcDir = new File(ctScan.getFolderLocation());
        File destinationDir = new File(ctScan.getNewFolderPath());
        org.apache.commons.io.FileUtils.moveDirectoryToDirectory(srcDir, destinationDir, true);
    }
}
