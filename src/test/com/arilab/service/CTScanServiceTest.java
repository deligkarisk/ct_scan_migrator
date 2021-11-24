package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.repository.CtScanRepository;
import com.arilab.utils.Config;
import com.arilab.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CTScanServiceTest {


    @Mock
    Config config;

    @Mock
    FileUtils fileUtils;

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
    void preprocessScanFolderLocation() {

        // given
        when(ctScan.getFolderLocation()).thenReturn("Y:bucket/CTScansFolder/Ants/Genus " +
                "Pheidole/CASENT334/CASENT334_2000_030302_000000");
        when(ctScan.getDicomFolderLocation()).thenReturn("Y:/bucket/CTScansFolder/Ants/Genus Pheidole/CASENT334_OUT");
        when(config.getSourceDirectory()).thenReturn("/mnt/bucket/CTScansFolder");

        // when
        ctScanService.preprocessScanFolderLocation(ctScan);

        // then
        verify(ctScan).setFolderLocation(stringArgumentCaptor.capture());
        assertEquals("/mnt/bucket/CTScansFolder/Ants/Genus Pheidole/CASENT334/CASENT334_2000_030302_000000",
                stringArgumentCaptor.getValue());
        verify(ctScan).setDicomFolderLocation(stringArgumentCaptor.capture());
        assertEquals("/mnt/bucket/CTScansFolder/Ants/Genus Pheidole/CASENT334_OUT", stringArgumentCaptor.getValue());
    }



    @Test
    void preprocessScanFolderLocationExceptionIfCannotFind() {

        // given
        when(ctScan.getFolderLocation()).thenReturn("Y:bucket/CTScansFolder/Ants/Genus " +
                "Pheidole/CASENT334/CASENT334_2000_030302_000000");
        when(config.getSourceDirectory()).thenReturn("/mnt/bucket/UexistentFolder");

        // when
        try {
            ctScanService.preprocessScanFolderLocation(ctScan);
        } catch (RuntimeException runtimeException) {
            return;

        }
        fail();
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
        when(fileUtils.folderExists(any())).thenReturn(false);


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
        when(fileUtils.folderExists(any())).thenReturn(false);


        // when
        ctScanService.setNewFolderPaths(ctScan);

        // then
        then(ctScan).should().setNewFolderPath(captor.capture());
        assertEquals(EXPECTED_NEW_MAIN_FOLDER, captor.getValue());
        then(ctScan).should(times(1)).setNewDicomFolderPath(captor.capture());
        assertEquals(EXPECTED_NEW_DICOM_FOLDER, captor.getValue());
    }




    @Test
    void setNewFolderPathsWithoutSpecialIdWithDicomWhenDicomFolderAlreadyExists() {
        String BASE_NAME = "CASENT0000_Phe_Head_20200802_000000";
        String EXPECTED_NEW_MAIN_FOLDER = "/mnt/bucket/Ants/Pheidole/Strumingenys/" + BASE_NAME;
        String EXPECTED_NEW_DICOM_FOLDER = EXPECTED_NEW_MAIN_FOLDER + "/" + BASE_NAME + "_dicom2";

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
        when(fileUtils.folderExists(any())).thenReturn(true);

        // when
        ctScanService.setNewFolderPaths(ctScan);

        // then
        then(ctScan).should().setNewFolderPath(captor.capture());
        assertEquals(EXPECTED_NEW_MAIN_FOLDER, captor.getValue());
        then(ctScan).should(times(1)).setNewDicomFolderPath(captor.capture());
        assertEquals(EXPECTED_NEW_DICOM_FOLDER, captor.getValue());
    }
}