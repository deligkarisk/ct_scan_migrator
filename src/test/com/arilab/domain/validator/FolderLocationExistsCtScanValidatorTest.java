package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FolderLocationExistsCtScanValidatorTest {

    FolderLocationExistsCtScanValidator folderLocationExistsValidator = new FolderLocationExistsCtScanValidator();

    @Test
    void validateFolderExists(@TempDir Path tempDir) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setFolderLocation(tempDir.toString());

        // when
        Optional<String> returnedResult = folderLocationExistsValidator.validate(ctScan);

        // then
        assertEquals(Optional.empty(), returnedResult);
    }

    @Test
    void validateFolderNotExists() {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setFolderLocation("randomNotFolder");

        // when
        Optional<String> returnedResult = folderLocationExistsValidator.validate(ctScan);

        // then
        assertEquals(Optional.of("Folder location does not exist."), returnedResult);
    }
}