package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DicomFolderLocationExistsValidatorTest {

    DicomFolderLocationExistsValidator dicomFolderLocationExistsValidator = new DicomFolderLocationExistsValidator();

    @Test
    void validateFolderIsNull() {
        // given
        CtScan ctScan = new CtScan();

        // when
        Optional<String> returnedResult = dicomFolderLocationExistsValidator.validate(ctScan);

        // then
        assertEquals(Optional.empty(), returnedResult);
    }

    @Test
    void validateFolderExists(@TempDir Path tempDir) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation(tempDir.toString());

        // when
        Optional<String> returnedResult = dicomFolderLocationExistsValidator.validate(ctScan);

        // then
        assertEquals(Optional.empty(), returnedResult);
    }

    @Test
    void validateFolderNotExists() {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation("randomNotFolder");

        // when
        Optional<String> returnedResult = dicomFolderLocationExistsValidator.validate(ctScan);

        // then
        assertEquals(Optional.of("Dicom folder location entered does not exist."), returnedResult);
    }
}