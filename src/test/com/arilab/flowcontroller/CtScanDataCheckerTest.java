package com.arilab.flowcontroller;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.system.SystemExit;
import com.arilab.utils.Config;
import com.arilab.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CtScanDataCheckerTest {

   CtScanCollection ctScanCollection;

    @Mock
    CtScan ctScan1;

    @Mock
    CtScan ctScan2;

    @Mock
    FileUtils fileUtils;

    @Mock
    Config config;

    @Mock
    SystemExit systemExit;

    @InjectMocks
    CtScanDataChecker ctScanDataChecker;

    @BeforeEach
    void setUp() {

        List<CtScan> scanList = new ArrayList<>();
        scanList.add(ctScan1);
        scanList.add(ctScan2);

        CtScanCollection ctScanCollection = new CtScanCollection(scanList);


    }

    @Test
    void checkThatIfNotValidDataItExits() {

        // given
        when(ctScan1.getAllinputDataIsValid()).thenReturn(true);
        when(ctScan2.getAllinputDataIsValid()).thenReturn(false);

        // when
        ctScanDataChecker.check(ctScanCollection);

        // then
        verify(systemExit).exit(1);
        verify(fileUtils).writeBeansToFile(any(), any());

    }

    @Test
    void testIfAllOkProceeds() {
        // given
        when(ctScan1.getAllinputDataIsValid()).thenReturn(true);
        when(ctScan2.getAllinputDataIsValid()).thenReturn(true);

        // when
        ctScanDataChecker.check(ctScanCollection);

        // then
        verifyNoInteractions(systemExit);
        verifyNoInteractions(fileUtils);


    }
}