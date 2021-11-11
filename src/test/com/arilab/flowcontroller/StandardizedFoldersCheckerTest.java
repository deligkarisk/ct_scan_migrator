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
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandardizedFoldersCheckerTest {

    @Mock
    Config config;

    @Mock
    SystemExit systemExit;

    @Mock
    FileUtils fileUtils;

    @InjectMocks
    StandardizedFoldersChecker standardizedFoldersChecker;

    @BeforeEach
    void setUp() {

    }

    @Test
    void checkIfAllAvailable() {

        // given
        CtScan ctScan1 = new CtScan();
        ctScan1.setNewFolderPathAvailable(true);
        ctScan1.setNewFolderPathAvailableIntheDatabase(true);

        CtScan ctScan2 = new CtScan();
        ctScan2.setNewFolderPathAvailable(true);
        ctScan2.setNewFolderPathAvailableIntheDatabase(true);

        List<CtScan> ctScans = new ArrayList<>();
        ctScans.add(ctScan1);
        ctScans.add(ctScan2);
        CtScanCollection ctScanCollection = new CtScanCollection(ctScans);


        // when
        standardizedFoldersChecker.check(ctScanCollection, "failedOutputFile");

        // then
        verifyNoInteractions(systemExit);

    }

    @Test
    void checkPathFoldersNotAvailable() {

        // given
        CtScan ctScan1 = new CtScan();
        ctScan1.setNewFolderPathAvailable(true);
        ctScan1.setNewFolderPathAvailableIntheDatabase(true);

        CtScan ctScan2 = new CtScan();
        ctScan2.setNewFolderPathAvailable(false);
        ctScan2.setNewFolderPathAvailableIntheDatabase(true);

        List<CtScan> ctScans = new ArrayList<>();
        ctScans.add(ctScan1);
        ctScans.add(ctScan2);
        CtScanCollection ctScanCollection = new CtScanCollection(ctScans);


        // when
        standardizedFoldersChecker.check(ctScanCollection, "failedOutputFile");

        // then
        verify(systemExit).exit(1);
    }


    @Test
    void checkPathInDatabaseNotAvailable() {

        // given
        CtScan ctScan1 = new CtScan();
        ctScan1.setNewFolderPathAvailable(true);
        ctScan1.setNewFolderPathAvailableIntheDatabase(true);

        CtScan ctScan2 = new CtScan();
        ctScan2.setNewFolderPathAvailable(true);
        ctScan2.setNewFolderPathAvailableIntheDatabase(false);

        List<CtScan> ctScans = new ArrayList<>();
        ctScans.add(ctScan1);
        ctScans.add(ctScan2);
        CtScanCollection ctScanCollection = new CtScanCollection(ctScans);


        // when
        standardizedFoldersChecker.check(ctScanCollection, "failedOutputFile");


        // then
        verify(systemExit).exit(1);
    }
}