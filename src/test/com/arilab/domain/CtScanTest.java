package com.arilab.domain;

import com.arilab.service.CTScanService;
import com.arilab.service.DatabaseService;
import com.arilab.utils.CtScanUtils;
import com.arilab.utils.PathUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CtScanTest {

    @Mock
    CtScanValidator ctScanValidator;


    @Mock
    CtScanUtils ctScanUtils;

    @Mock
    PathUtils pathUtils;

    @Mock
    CTScanService ctScanService;







    @Test
    void validateStandardizedFolderAllValid() throws SQLException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setNewFolderPath("folder"); // value is irrelevant as the pathUtils has been mocked.
        when(pathUtils.folderExists(any())).thenReturn(false);
        when(ctScanService.ctScanFolderExists(anyString())).thenReturn(false);

        // when
        ctScan.validateStandardizedFolder(pathUtils, ctScanService);

        // then
        assertEquals(true, ctScan.getNewFolderPathAvailable());
        assertEquals(true, ctScan.getNewFolderPathAvailableIntheDatabase());
    }



    @Test
    void validateStandardizedFolderSomeInvalid() throws SQLException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setNewFolderPath("folder"); // value is irrelevant as the pathUtils has been mocked.
        when(pathUtils.folderExists(any())).thenReturn(true);
        when(ctScanService.ctScanFolderExists(anyString())).thenReturn(false);

        // when
        ctScan.validateStandardizedFolder(pathUtils, ctScanService);

        // then
        assertEquals(false, ctScan.getNewFolderPathAvailable());
        assertEquals(true, ctScan.getNewFolderPathAvailableIntheDatabase());
    }
}