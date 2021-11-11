package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.repository.CtScanRepository;
import com.arilab.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CTScanMigratorServiceTest {


    @Mock
    FileUtils fileUtils;

    @Mock
    CtScanRepository ctScanRepository;



    @InjectMocks
    CTScanMigratorService ctScanMigratorService;


    @Test
    void migrateScans() throws SQLException, IOException {
        // given
        CtScan ctScan1 = new CtScan();
        CtScan ctScan2 = new CtScan();
        CtScan ctScan3 = new CtScan();

        List<CtScan> ctScans = new ArrayList<>();
        ctScans.add(ctScan1);
        ctScans.add(ctScan2);
        ctScans.add(ctScan3);

        CtScanCollection ctScanCollection = new CtScanCollection(ctScans);

        // when
        ctScanMigratorService.migrateScans(ctScanCollection, false);


        // then
        assertEquals(true, ctScan1.getMigrated());
        assertEquals(true, ctScan2.getMigrated());
        assertEquals(true, ctScan3.getMigrated());
        verify(ctScanRepository, times(3)).insertCtScan(any(), any());
        verify(fileUtils, times(3)).migrateFolder(any(),any());
    }
}