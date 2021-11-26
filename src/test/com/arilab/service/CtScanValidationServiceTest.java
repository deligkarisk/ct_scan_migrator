package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.validator.CtScanValidator;
import com.arilab.domain.validator.error.ErrorModel;
import com.arilab.domain.validator.group.BasicFieldValidationGroup;
import com.arilab.domain.validator.group.StandardizedFoldersValidationGroup;
import com.arilab.domain.validator.group.ValidatorGroup;
import com.arilab.repository.DatabaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CtScanValidationServiceTest {

    @Mock
    DatabaseService databaseService;

    @Mock
    CTScanService ctScanService;

    CtScanValidationService ctScanValidationService = new CtScanValidationService();


    @Test
    void validateResultsInNoErrorsIfScanDataAreCorrectWithAllValidationGroups(@TempDir File tempDir) throws SQLException {
        // todo: update code to check that if a new validation group is added without this test being updated, the
        //  test will fail. Maybe with reflection?

        // given
        when(databaseService.specimenCodeExists("CASENT000")).thenReturn(true);
        when(ctScanService.ctScanFolderExists("/mnt/NewPath")).thenReturn(false);


        CtScan ctScan = new CtScan();
        ctScan.setSpecimenCode("CASENT000");
        ctScan.setWet("Yes");
        ctScan.setEthanolConcentration("99%");
        ctScan.setStaining("Iodine");
        ctScan.setAntscan("No");
        ctScan.setModel("Ants");
        ctScan.setBodyPart("Whole_body");
        ctScan.setFolderLocation(tempDir.toString());
        ctScan.setNewFolderPath("/mnt/NewPath");

        ValidatorGroup<CtScanValidator> basicFieldValidationGroup = new BasicFieldValidationGroup(databaseService);
        ValidatorGroup<CtScanValidator> standardizedFoldersValidationGroup = new StandardizedFoldersValidationGroup(ctScanService);

        // when
        ErrorModel returnedErrorModelBasicField = ctScanValidationService.validate(basicFieldValidationGroup, ctScan);
        ErrorModel returnedErrorModelStandardizedFolders = ctScanValidationService.validate(standardizedFoldersValidationGroup, ctScan);


        // then
        assertFalse(returnedErrorModelBasicField.hasErrors());
        assertFalse(returnedErrorModelStandardizedFolders.hasErrors());

    }



    @Test
    void validateResultsInErrorsIfScanDataAreNotCorrectWithBasicFieldValidationGroup(@TempDir File tempDir,
                                                                                     @TempDir File targetDir) throws SQLException {
        // given
        int expectedNumberOfErrorsInBasicFieldValidation = 3;
        int expectedNumberOfErrorsInStandardizedFoldersValidation = 2;

        String expectedBasicFieldValidationError1 = "CtScan: " + tempDir.toString() + " has the error: Wet/dry combinations not correct.";
        String expectedBasicFieldValidationError2 = "CtScan: " + tempDir.toString() + " has the error: Antscan coding is not correct.";
        String expectedBasicFieldValidationError3 = "CtScan: " + tempDir.toString() + " has the error: Wrong value in the Body part field.";

        String expectedStandardizedFoldersValidationError1 = "CtScan: " + tempDir.toString() + " has the error: Standardized path is not available in the database.";
        String expectedStandardizedFoldersValidationError2 = "CtScan: " + tempDir.toString() + " has the error: Standardized path is not available in the filesystem.";

        when(databaseService.specimenCodeExists("CASENT000")).thenReturn(true);
        when(ctScanService.ctScanFolderExists(targetDir.toString())).thenReturn(true);

        CtScan ctScan = new CtScan();
        ctScan.setSpecimenCode("CASENT000");
        ctScan.setWet("No");
        ctScan.setEthanolConcentration("99%");
        ctScan.setStaining("Iodine");
        ctScan.setAntscan("Yes");
        ctScan.setModel("Ants");
        ctScan.setBodyPart("Unknown Body Part");
        ctScan.setFolderLocation(tempDir.toString());
        ctScan.setNewFolderPath(targetDir.toString());

        ValidatorGroup<CtScanValidator> basicFieldValidationGroup = new BasicFieldValidationGroup(databaseService);
        ValidatorGroup<CtScanValidator> standardizedFoldersValidationGroup = new StandardizedFoldersValidationGroup(ctScanService);

        // when
        ErrorModel returnedErrorModelBasicField = ctScanValidationService.validate(basicFieldValidationGroup, ctScan);
        ErrorModel returnedErrorModelStandardizedFolders = ctScanValidationService.validate(standardizedFoldersValidationGroup, ctScan);

        // then
        assertTrue(returnedErrorModelBasicField.hasErrors());
        assertEquals(expectedNumberOfErrorsInBasicFieldValidation, returnedErrorModelBasicField.getNumberOfErrors());
        assertTrue(returnedErrorModelBasicField.printErrors().contains(expectedBasicFieldValidationError1));
        assertTrue(returnedErrorModelBasicField.printErrors().contains(expectedBasicFieldValidationError2));
        assertTrue(returnedErrorModelBasicField.printErrors().contains(expectedBasicFieldValidationError3));

        assertTrue(returnedErrorModelStandardizedFolders.hasErrors());
        assertEquals(expectedNumberOfErrorsInStandardizedFoldersValidation, returnedErrorModelStandardizedFolders.getNumberOfErrors());
        assertTrue(returnedErrorModelStandardizedFolders.printErrors().contains(expectedStandardizedFoldersValidationError1));
        assertTrue(returnedErrorModelStandardizedFolders.printErrors().contains(expectedStandardizedFoldersValidationError2));
    }
}