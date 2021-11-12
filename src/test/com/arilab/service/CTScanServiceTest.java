package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanValidator;
import com.arilab.repository.CtScanRepository;
import com.arilab.utils.Config;
import com.arilab.utils.PathUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CTScanServiceTest {


    @Mock
    Config config;

    @Mock
    PathUtils pathUtils;

    @Mock
    CtScanValidator ctScanValidator;

    @Mock
    DatabaseService databaseService;

    @Spy
    CtScan ctScan;

    @Mock
    CtScanRepository ctScanRepository;

    @InjectMocks
    @Spy
    CTScanService ctScanService;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;


    @Test
    void preprocessScanFolderLocation() throws SQLException {

        // given
        when(ctScan.getFolderLocation()).thenReturn("/mnt/bucket/CTScans/Ants/Genus " +
                "Pheidole/CASENT334/CASENT334_2000_030302_000000");
        when(ctScan.getDicomFolderLocation()).thenReturn("/mnt/bucket/CTScans/Ants/Genus Pheidole/CASENT334_OUT");
        when(pathUtils.fixPrependPath(any())).thenReturn(Paths.get("path"));

        // when
        ctScanService.preprocessScanFolderLocation(ctScan);

        // then
        verify(ctScan).setFolderLocation(stringArgumentCaptor.capture());
        assertEquals("path", stringArgumentCaptor.getValue());
        verify(ctScan).setDicomFolderLocation(stringArgumentCaptor.capture());
        assertEquals("path", stringArgumentCaptor.getValue());
    }


    @Test
    void validateScanDataAllValid() throws SQLException {
        // given
        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        when(ctScanValidator.specimenCodeExists(any())).thenReturn(true);
        when(ctScanValidator.wetDryCombinationIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.dryMethodCheck(any())).thenReturn(true);
        when(ctScanValidator.bodypartCheck(any())).thenReturn(true);
        when(ctScanValidator.folderLocationExists(any())).thenReturn(true);
        when(ctScanValidator.dicomFolderLocationExists(any())).thenReturn(true);
        when(ctScanValidator.modelIsAnts(any())).thenReturn(true);
        when(ctScanValidator.stainingIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.ethanolConcIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.antscanIsCorrect(any())).thenReturn(true);
        when(ctScanValidator.dicomFolderNotInMainFolder(any())).thenReturn(true);


        // when
        ctScanService.validateScanData(ctScan);

        // then
        verify(ctScan).setAllinputDataIsValid(booleanArgumentCaptor.capture());
        assertEquals(true, booleanArgumentCaptor.getValue());
    }


    @Test
    void validateScanDataWithInvalid() throws SQLException {
        // given
        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
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


        // when
        ctScanService.validateScanData(ctScan);

        // then
        verify(ctScan).setAllinputDataIsValid(booleanArgumentCaptor.capture());
        assertEquals(false, booleanArgumentCaptor.getValue());
        verify(ctScan).setEthanolConcIsCorrect(booleanArgumentCaptor.capture());
        assertEquals(true, booleanArgumentCaptor.getValue());
    }


    @Test
    void setNewFolderPathsWithSpecialIdNullDicom() {
        // given
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(databaseService.findGenusFromSpecimenCode(any())).thenReturn("Pheidole");
        when(databaseService.findSpeciesNameOrMorphoCodeFromSpecimenCode(any())).thenReturn("Strumingenys");
        when(ctScan.getSpecimenCode()).thenReturn("CASENT0000");
        when(ctScan.getBodyPart()).thenReturn("Head");
        when(ctScan.getTimestamp()).thenReturn("20200802_000000");
        when(ctScan.getSpecialIdentifier()).thenReturn("SpecialID");
        when(ctScan.getModel()).thenReturn("Ants");
        when(config.getTargetDirectory()).thenReturn("/mnt/bucket/");

        // when
        ctScanService.setNewFolderPaths(ctScan);

        // then
        then(ctScan).should().setNewFolderPath(captor.capture());
        assertEquals("/mnt/bucket/Ants/Pheidole/Strumingenys/CASENT0000_Phe_Head_SpecialID_20200802_000000", captor.getValue());
        then(ctScan).should(times(0)).setNewDicomFolderPath(any());
    }

    @Test
    void setNewFolderPathsWithoutSpecialIdNullDicom() {
        // given
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(databaseService.findGenusFromSpecimenCode(any())).thenReturn("Pheidole");
        when(databaseService.findSpeciesNameOrMorphoCodeFromSpecimenCode(any())).thenReturn("Strumingenys");
        when(ctScan.getSpecimenCode()).thenReturn("CASENT0000");
        when(ctScan.getBodyPart()).thenReturn("Head");
        when(ctScan.getTimestamp()).thenReturn("20200802_000000");
        when(ctScan.getModel()).thenReturn("Ants");
        when(config.getTargetDirectory()).thenReturn("/mnt/bucket/");

        // when
        ctScanService.setNewFolderPaths(ctScan);

        // then
        then(ctScan).should().setNewFolderPath(captor.capture());
        assertEquals("/mnt/bucket/Ants/Pheidole/Strumingenys/CASENT0000_Phe_Head_20200802_000000", captor.getValue());
        then(ctScan).should(times(0)).setNewDicomFolderPath(any());
    }

    @Test
    void setNewFolderPathsWithSpecialIdWithDicom() {

        String BASE_NAME = "CASENT0000_Phe_Head_SpecialID_20200802_000000";
        String EXPECTED_NEW_MAIN_FOLDER = "/mnt/bucket/Ants/Pheidole/Strumingenys/" + BASE_NAME;
        String EXPECTED_NEW_DICOM_FOLDER = EXPECTED_NEW_MAIN_FOLDER + "/" + BASE_NAME + "_dicom";

        // given
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(databaseService.findGenusFromSpecimenCode(any())).thenReturn("Pheidole");
        when(databaseService.findSpeciesNameOrMorphoCodeFromSpecimenCode(any())).thenReturn("Strumingenys");
        when(ctScan.getSpecimenCode()).thenReturn("CASENT0000");
        when(ctScan.getBodyPart()).thenReturn("Head");
        when(ctScan.getTimestamp()).thenReturn("20200802_000000");
        when(ctScan.getSpecialIdentifier()).thenReturn("SpecialID");
        when(ctScan.getModel()).thenReturn("Ants");
        when(ctScan.getDicomFolderLocation()).thenReturn("/mnt/bucket/CASENT000_dicom");
        when(config.getTargetDirectory()).thenReturn("/mnt/bucket/");

        // when
        ctScanService.setNewFolderPaths(ctScan);

        // then
        then(ctScan).should().setNewFolderPath(captor.capture());
        assertEquals(EXPECTED_NEW_MAIN_FOLDER, captor.getValue());
        then(ctScan).should(times(1)).setNewDicomFolderPath(captor.capture());
        assertEquals(EXPECTED_NEW_DICOM_FOLDER, captor.getValue());
    }

    @Test
    void setNewFolderPathsWithoutSpecialIdWithDicom() {
        String BASE_NAME = "CASENT0000_Phe_Head_20200802_000000";
        String EXPECTED_NEW_MAIN_FOLDER = "/mnt/bucket/Ants/Pheidole/Strumingenys/" + BASE_NAME;
        String EXPECTED_NEW_DICOM_FOLDER = EXPECTED_NEW_MAIN_FOLDER + "/" + BASE_NAME + "_dicom";

        // given
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(databaseService.findGenusFromSpecimenCode(any())).thenReturn("Pheidole");
        when(databaseService.findSpeciesNameOrMorphoCodeFromSpecimenCode(any())).thenReturn("Strumingenys");
        when(ctScan.getSpecimenCode()).thenReturn("CASENT0000");
        when(ctScan.getBodyPart()).thenReturn("Head");
        when(ctScan.getTimestamp()).thenReturn("20200802_000000");
        when(ctScan.getModel()).thenReturn("Ants");
        when(ctScan.getDicomFolderLocation()).thenReturn("/mnt/bucket/CASENT000_dicom");
        when(config.getTargetDirectory()).thenReturn("/mnt/bucket/");

        // when
        ctScanService.setNewFolderPaths(ctScan);

        // then
        then(ctScan).should().setNewFolderPath(captor.capture());
        assertEquals(EXPECTED_NEW_MAIN_FOLDER, captor.getValue());
        then(ctScan).should(times(1)).setNewDicomFolderPath(captor.capture());
        assertEquals(EXPECTED_NEW_DICOM_FOLDER, captor.getValue());
    }


    @Test
    void validateStandardizedFolderAllValid() throws SQLException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setNewFolderPath("folder"); // value is irrelevant as the pathUtils has been mocked.
        when(ctScanValidator.standardizedFolderIsAvailable(any())).thenReturn(true);
        //when(ctScanService.ctScanFolderExists(any())).thenReturn(false);
        Mockito.doReturn(false).when(ctScanService).ctScanFolderExists(any());

        // when
        ctScanService.validateStandardizedFolder(ctScan);

        // then
        assertEquals(true, ctScan.getNewFolderPathAvailable());
        assertEquals(true, ctScan.getNewFolderPathAvailableIntheDatabase());
    }


    @Test
    void validateStandardizedFolderSomeInvalid() throws SQLException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setNewFolderPath("folder"); // value is irrelevant as the pathUtils has been mocked.
        when(ctScanValidator.standardizedFolderIsAvailable(any())).thenReturn(false);
        Mockito.doReturn(false).when(ctScanService).ctScanFolderExists(any());

        // when
        ctScanService.validateStandardizedFolder(ctScan);


        // then
        assertEquals(false, ctScan.getNewFolderPathAvailable());
        assertEquals(true, ctScan.getNewFolderPathAvailableIntheDatabase());
    }
}