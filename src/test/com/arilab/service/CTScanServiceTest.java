package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.utils.Config;
import com.arilab.utils.PathUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CTScanServiceTest {


    private CtScan ctScan;

    @Mock
    Config config;

    @Mock
    PathUtils pathUtils;

    @InjectMocks
    CTScanService ctScanService;



    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        ctScan = new CtScan();
        ctScan.setFolderLocation("/mnt/bucket/CTScans/Ants/Genus Pheidole/CASENT334/CASENT334_2000_030302_000000");
        ctScan.setDicomFolderLocation("/mnt/bucket/CTScans/Ants/Genus Pheidole/CASENT334_OUT");


       // when(config.getSourceDirectory())
    }

    @Test
    void preprocessScanFolderLocation() {

        // when
        ctScanService.preprocessScanFolderLocation(ctScan);

        // then
        verify(ctScan).setDicomFolderLocation(stringArgumentCaptor.capture());
       // assertEquals();



    }


}