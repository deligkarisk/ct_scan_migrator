package com.arilab.reader;

import com.arilab.domain.CtScan;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SourceReader {

    private Logger logger = LoggerFactory.getLogger(SourceReader.class);

    public List readScans(String file) {
        try {
            Reader fileReader = Files.newBufferedReader(Path.of(file));
            List csvToBean = new CsvToBeanBuilder(fileReader).withType(CtScan.class).withIgnoreLeadingWhiteSpace(
                    true).withFieldAsNull(CSVReaderNullFieldIndicator.BOTH).build().parse();
            logger.info("Found " + csvToBean.size() + " scans");
            return csvToBean;
        } catch (IOException exception) {
            logger.error("Could not find data file " + exception.toString());
            System.exit(1);
        }
        return null;
    }
}
