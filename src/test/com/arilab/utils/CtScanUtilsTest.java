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

    @Test
    void findStandardizedFolderNameWithSpecialID() {
        // given
        when(databaseService.findGenusFromSpecimenCode(any())).thenReturn("Pheidole");
        when(databaseService.findSpeciesNameOrMorphoCodeFromSpecimenCode(any())).thenReturn("Strumingenys");

        when(config.getTargetDirectory()).thenReturn("/mnt/bucket/");

        CtScan ctScan = new CtScan();
        ctScan.setSpecimenCode("CASENT0000");
        ctScan.setBodyPart("Head");
        ctScan.setTimestamp("20200802_000000");
        ctScan.setSpecialIdentifier("SpecialID");
        ctScan.setModel("Ants");

        // when
        String returnedStandardizedFolderName = ctScanUtils.findStandardizedFolderName(ctScan);

        // then
        assertEquals("/mnt/bucket/Ants/Pheidole/Strumingenys/CASENT0000_Phe_Head_SpecialID_20200802_000000", returnedStandardizedFolderName);

    }

    @Test
    void findStandardizedFolderNameWithOutSpecialID() {
        // given
        when(databaseService.findGenusFromSpecimenCode(any())).thenReturn("Pheidole");
        when(databaseService.findSpeciesNameOrMorphoCodeFromSpecimenCode(any())).thenReturn("Strumingenys");

        when(config.getTargetDirectory()).thenReturn("/mnt/bucket/");

        CtScan ctScan = new CtScan();
        ctScan.setSpecimenCode("CASENT0000");
        ctScan.setBodyPart("Head");
        ctScan.setTimestamp("20200802_000000");
        ctScan.setModel("Ants");

        // when
        String returnedStandardizedFolderName = ctScanUtils.findStandardizedFolderName(ctScan);

        // then
        assertEquals("/mnt/bucket/Ants/Pheidole/Strumingenys/CASENT0000_Phe_Head_20200802_000000", returnedStandardizedFolderName);

    }
}