package com.arilab.service;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollectionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ctScanCollectionValidatorTest {


    @InjectMocks
    CtScanCollectionValidator ctScanCollectionValidator;

    @Test
    void allFoldersUnique() {

        // given
        CtScan ctScan1 = new CtScan();
        ctScan1.setFolderLocation("old_folder_1");
        ctScan1.setNewFolderPath("new_folder_path_1");
        ctScan1.setDicomFolderLocation("dicom_folder_location_1");

        CtScan ctScan2 = new CtScan();
        ctScan2.setFolderLocation("old_folder_2");
        ctScan2.setNewFolderPath("new_folder_path_2");
        ctScan2.setDicomFolderLocation("dicom_folder_location_2");

        List<CtScan> ctScans = new ArrayList<>();
        ctScans.add(ctScan1);
        ctScans.add(ctScan2);

        // when
        boolean returnedResult = ctScanCollectionValidator.areAllFoldersUniqueInCollection(ctScans);

        // then
        assertEquals(true, returnedResult);
    }



    @ParameterizedTest
    @CsvSource({
            "old_folder_1, new_folder_path_1, dicom_folder_location_1,old_folder_1, new_folder_path_2, " +
                    "dicom_folder_location_2",
            "old_folder_1, new_folder_path_1, dicom_folder_location_1,old_folder_2, new_folder_path_1, " +
                    "dicom_folder_location_2",
            "old_folder_1, new_folder_path_1, dicom_folder_location_1,old_folder_2, new_folder_path_2, " +
                    "dicom_folder_location_1",

    })
    public void foldersNotUnique(String oldFolder1, String newFolder1, String dicomFolder1, String oldFolder2,
                          String newFolder2, String dicomFolder2) {

        // given
        CtScan ctScan1 = new CtScan();
        ctScan1.setFolderLocation(oldFolder1);
        ctScan1.setNewFolderPath(newFolder1);
        ctScan1.setDicomFolderLocation(dicomFolder1);

        CtScan ctScan2 = new CtScan();
        ctScan2.setFolderLocation(oldFolder2);
        ctScan2.setNewFolderPath(newFolder2);
        ctScan2.setDicomFolderLocation(dicomFolder2);

        List<CtScan> ctScans = new ArrayList<>();
        ctScans.add(ctScan1);
        ctScans.add(ctScan2);

        // when
        boolean returnedResult = ctScanCollectionValidator.areAllFoldersUniqueInCollection(ctScans);

        // then
        assertEquals(false, returnedResult);
    }



}





