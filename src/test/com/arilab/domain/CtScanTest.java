package com.arilab.domain;

import com.arilab.service.CTScanService;
import com.arilab.service.DatabaseService;
import com.arilab.utils.CtScanUtils;
import com.arilab.utils.PathUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    void validateScanDataAllValid() throws SQLException {
        // given
        when(ctScanValidator.specimenCodeExists(any())).thenReturn(true);
        when(ctScanValidator.wetDryCombinationIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.dryMethodCheck(any())).thenReturn(true);
        when(ctScanValidator.bodypartCheck(any())).thenReturn(true);
        when(ctScanValidator.folderLocationExists(any())).thenReturn(true);
        when(ctScanValidator.modelIsAnts(any())).thenReturn(true);
        when(ctScanValidator.stainingIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.ethanolConcIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.antscanIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.dicomFolderNotInMainFolder(any())).thenReturn(true);

        CtScan ctScan = new CtScan();

        // when
        ctScan.validateScanData(ctScanValidator);

        // then
        assertEquals(true, ctScan.getAllinputDataIsValid());
        assertEquals(true, ctScan.getSpecimenCodeExists());
        assertEquals(true, ctScan.getWetDryCombinationIsCorrect());
        assertEquals(true, ctScan.getDryMethodIsCorrect());
        assertEquals(true, ctScan.getBodypartIsCorrect());
        assertEquals(true, ctScan.getFolderLocationExists());
        assertEquals(true, ctScan.getModelIsCorrect());
        assertEquals(true, ctScan.getStainingIsCorrect());
        assertEquals(true, ctScan.getEthanolConcIsCorrect());
        assertEquals(true, ctScan.getAntscanCodingIsCorrect());
        assertEquals(true, ctScan.getDicomFolderNotAChildOfMain());

    }


    @Test
    void validateScanDataWithInvalid() throws SQLException {
        // given
        when(ctScanValidator.specimenCodeExists(any())).thenReturn(false);
        when(ctScanValidator.wetDryCombinationIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.dryMethodCheck(any())).thenReturn(true);
        when(ctScanValidator.bodypartCheck(any())).thenReturn(true);
        when(ctScanValidator.folderLocationExists(any())).thenReturn(true);
        when(ctScanValidator.modelIsAnts(any())).thenReturn(true);
        when(ctScanValidator.stainingIsCorrect(any())).thenReturn(false);
        when(ctScanValidator.ethanolConcIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.antscanIsCorrect(any())).thenReturn(false);
        when(ctScanValidator.dicomFolderNotInMainFolder(any())).thenReturn(true);

        CtScan ctScan = new CtScan();

        // when
        ctScan.validateScanData(ctScanValidator);

        // then
        assertEquals(false, ctScan.getAllinputDataIsValid());
        assertEquals(false, ctScan.getSpecimenCodeExists());
        assertEquals(true, ctScan.getWetDryCombinationIsCorrect());
        assertEquals(true, ctScan.getDryMethodIsCorrect());
        assertEquals(true, ctScan.getBodypartIsCorrect());
        assertEquals(true, ctScan.getFolderLocationExists());
        assertEquals(true, ctScan.getModelIsCorrect());
        assertEquals(false, ctScan.getStainingIsCorrect());
        assertEquals(true, ctScan.getEthanolConcIsCorrect());
        assertEquals(false, ctScan.getAntscanCodingIsCorrect());
        assertEquals(true, ctScan.getDicomFolderNotAChildOfMain());

    }

    @Test
    void findStandardizedFolderName() {

        // given
        when(ctScanUtils.findStandardizedFolderName(any())).thenReturn("folderspath");
        CtScan ctScan = new CtScan();

        // when
        ctScan.findStandardizedFolderName(ctScanUtils);

        // then
        assertEquals("folderspath", ctScan.getNewFolderPath());
    }


    @Test
    void validateStandardizedFolderAllValid() throws SQLException {
        // given
        CtScan ctScan = new CtScan();
        when(pathUtils.folderExists(any())).thenReturn(true);
        when(ctScanService.ctScanFolderExists(anyString())).thenReturn(true);

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
        when(pathUtils.folderExists(any())).thenReturn(false);
        when(ctScanService.ctScanFolderExists(anyString())).thenReturn(true);

        // when
        ctScan.validateStandardizedFolder(pathUtils, ctScanService);

        // then
        assertEquals(false, ctScan.getNewFolderPathAvailable());
        assertEquals(true, ctScan.getNewFolderPathAvailableIntheDatabase());
    }
}