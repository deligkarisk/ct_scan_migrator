package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import com.arilab.service.CTScanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandardizedPathAvailableInDBTest {

    @Mock
    CTScanService ctScanService;

    @InjectMocks
    StandardizedPathAvailableInDB standardizedPathAvailableInDB;

    @Test
    void validateReturnsEmptyIfNoErrors() throws SQLException {
        // given
        when(ctScanService.ctScanFolderExists(any())).thenReturn(false);
        CtScan ctScan = new CtScan();
        ctScan.setNewFolderPath("testNewFolderPaths");

        // when
        Optional<String> returnedValue = standardizedPathAvailableInDB.validate(ctScan);

        // then
        assertEquals(Optional.empty(), returnedValue);
    }

    @Test
    void validateReturnsErrorIfFolderAlreadyExists() throws SQLException {
        // given
        when(ctScanService.ctScanFolderExists(any())).thenReturn(true);
        CtScan ctScan = new CtScan();
        ctScan.setNewFolderPath("testNewFolderPaths");

        // when
        Optional<String> returnedValue = standardizedPathAvailableInDB.validate(ctScan);

        // then
        assertEquals(Optional.of("Standardized path is not available in the database."), returnedValue);

    }
}