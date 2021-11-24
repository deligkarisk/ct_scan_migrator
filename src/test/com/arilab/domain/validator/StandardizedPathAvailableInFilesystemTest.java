package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StandardizedPathAvailableInFilesystemTest {

    StandardizedPathAvailableInFilesystem standardizedPathAvailableInFilesystem =
            new StandardizedPathAvailableInFilesystem();

    @Test
    void validateEmptyIfFolderIsAvailable() throws SQLException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setNewFolderPath("notExistingFolder");

        // when
        Optional<String> returnedResult = standardizedPathAvailableInFilesystem.validate(ctScan);

        // then
        assertEquals(Optional.empty(), returnedResult);
    }

    @Test
    void validateErrorIfFolderIsNotAvailable(@TempDir File tempDir) throws SQLException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setNewFolderPath(tempDir.toString());

        // when
        Optional<String> returnedResult = standardizedPathAvailableInFilesystem.validate(ctScan);

        // then
        assertEquals(Optional.of("Standardized path is not available in the filesystem."), returnedResult);
    }
}