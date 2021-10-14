package com.arilab.utils;

import com.arilab.system.SystemExit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathUtilsTest {

    private final String PROP_SOURCE_DIRECTORY = "/mnt/bucket_dev/CT_Scan_2017-2019"; // The source directory
    // prepend, in the config.properties file.
    private  String currentDirectory; // The current directory, as the
    // user has entered it in the Excel file.

    private final String CORRECT_MODIFIED_DIRECTORY = "/mnt/bucket_dev/CT_Scan_2017-2019/Ants/Genus Strumigenys/S" +
            ".tumida_CASENT00185699/S.tumida_CASENT00185699_2018-06-30_113210";

    @Mock
    Config config;

    @Mock
    SystemExit systemExit;

    @InjectMocks
    PathUtils pathUtils;

    @BeforeEach
    void setUp() {
        when(config.getSourceDirectory()).thenReturn(PROP_SOURCE_DIRECTORY);

    }

    @Test
    void fixPrependPath() {

        currentDirectory = "/bucket/EconomoU/CT_Scan_2017-2019/Ants/Genus Strumigenys/S.tumida_CASENT00185699/S.tumida_CASENT00185699_2018-06-30_113210";

        Path returnedPath = pathUtils.fixPrependPath(currentDirectory);
        assertEquals(CORRECT_MODIFIED_DIRECTORY,returnedPath.toString());
    }

    @Test
    void whenCurrentLocationInvalidThenQuit() {
        currentDirectory = "/bucket/EconomoU/CT_Scan_/Ants/Genus Strumigenys/S.tumida_CASENT00185699/S" +
                ".tumida_CASENT00185699_2018-06-30_113210";

        Path returnedPath = pathUtils.fixPrependPath(currentDirectory);
        then(systemExit).should().exit(1);
    }
}