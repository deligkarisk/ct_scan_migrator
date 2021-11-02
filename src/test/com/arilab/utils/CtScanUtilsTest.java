package com.arilab.utils;

import com.arilab.domain.CtScan;
import com.arilab.service.DatabaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CtScanUtilsTest {


    @Mock
    DatabaseService databaseService;

    @Mock
    Config config;

    @InjectMocks
    CtScanUtils ctScanUtils;


    @Test
    void findDicomFolderLocation() {
    }

    @Test
    void createTimestampFromScanDate() {
    }


}