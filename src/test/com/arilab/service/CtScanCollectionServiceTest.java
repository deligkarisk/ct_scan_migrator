package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import com.arilab.domain.validator.CtScanValidator;
import com.arilab.domain.validator.error.ErrorModel;
import com.arilab.domain.validator.group.BasicFieldValidationGroup;
import com.arilab.domain.validator.group.StandardizedFoldersValidationGroup;
import com.arilab.domain.validator.group.ValidatorGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CtScanCollectionServiceTest {

    @Mock
    CTScanService ctScanService;

    @Mock
    DatabaseService databaseService;

    CtScanCollection ctScanCollection;

    CtScanCollectionService ctScanCollectionService = new CtScanCollectionService(ctScanService);


    @BeforeEach
    void setUp() {

    }

    // This tests only some of the validator groups, but should be enough to verify correct operation.
    @Test
    void validateCollectionAtScanLevelNoErrorsIfScanDataCorrect(@TempDir File tempDir, @TempDir File tempDir2) throws SQLException {
        // given
        int numberOfScans = 2;

        when(databaseService.specimenCodeExists("CASENT000")).thenReturn(true);
        when(databaseService.specimenCodeExists("CASENT123")).thenReturn(true);

        when(ctScanService.ctScanFolderExists("/mnt/NewPath")).thenReturn(false);
        when(ctScanService.ctScanFolderExists("/mnt/NewPath")).thenReturn(false);


        CtScan ctScan1 = new CtScan("CASENT000", "99%", "Yes", "Whole_body",
                tempDir.toString(), "Iodine", "No", null, "Ants", null, "/mnt/NewPath");
        CtScan ctScan2 = new CtScan("CASENT123", "No ethanol used", "No", "Whole_body",
                tempDir2.toString(), "Iodine", "No", null, "Ants", "Pin", "/mnt/NewPath2");
        List<CtScan> scanList = new LinkedList<>();
        scanList.add(ctScan1);
        scanList.add(ctScan2);
        ctScanCollection = new CtScanCollection(scanList);


        ValidatorGroup<CtScanValidator> basicFieldValidationGroup = new BasicFieldValidationGroup(databaseService);
        ValidatorGroup<CtScanValidator> standardizedFoldersValidationGroup = new StandardizedFoldersValidationGroup(ctScanService);

        // when
        List<ErrorModel> returnedErrorModelBasicField =
                ctScanCollectionService.validateCollectionAtScanLevel(basicFieldValidationGroup, ctScanCollection);

        List<ErrorModel> returnedErrorModelStandardizedFolders =
                ctScanCollectionService.validateCollectionAtScanLevel(standardizedFoldersValidationGroup, ctScanCollection);

        // then
        assertEquals(numberOfScans, ctScanCollection.getCtScans().size());
        assertEquals(0, returnedErrorModelBasicField.size());
        assertEquals(0, returnedErrorModelStandardizedFolders.size());

    }



    @Test
    // todo: Fix this one
    void validateCollectionAtScanLevelWithErrorsIfDataNotCorrect(@TempDir File tempDir, @TempDir File tempDir2) throws SQLException {
        // given
        when(databaseService.specimenCodeExists("CASENT000")).thenReturn(true);
        when(databaseService.specimenCodeExists("CASENT123")).thenReturn(true);

        when(ctScanService.ctScanFolderExists("/mnt/NewPath")).thenReturn(false);
        when(ctScanService.ctScanFolderExists("/mnt/NewPath2")).thenReturn(false);


        CtScan ctScan1 = new CtScan("CASENT000", "99%", "Yes", "Whole_body",
                tempDir.toString(), "Iodine", "No", null, "Ants", null, "/mnt/NewPath");
        CtScan ctScan2 = new CtScan("CASENT123", "No ethanol used", "No", "Whole_body",
                tempDir2.toString(), "Iodine", "No", null, "Ants", "Pin", "/mnt/NewPath2");


        ValidatorGroup<CtScanValidator> basicFieldValidationGroup = new BasicFieldValidationGroup(databaseService);
        ValidatorGroup<CtScanValidator> standardizedFoldersValidationGroup = new StandardizedFoldersValidationGroup(ctScanService);

        // when
        List<ErrorModel> returnedErrorModelBasicField =
                ctScanCollectionService.validateCollectionAtScanLevel(basicFieldValidationGroup, ctScanCollection);

        List<ErrorModel> returnedErrorModelStandardizedFolders =
                ctScanCollectionService.validateCollectionAtScanLevel(standardizedFoldersValidationGroup, ctScanCollection);

        // then
        assertEquals(0, returnedErrorModelBasicField.size());
        assertEquals(0, returnedErrorModelStandardizedFolders);

    }





    @Test
    void validateCollection() {
    }

    @Test
    void findStandardizedFolderNames() {
        fail();
    }
}