package com.arilab.service;

import com.arilab.domain.CtScan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    void FolderLocationNotUnique() {

        // given

        CtScan ctScan1 = new CtScan();
        ctScan1.setFolderLocation("old_folder_1");
        ctScan1.setNewFolderPath("new_folder_path_1");
        ctScan1.setDicomFolderLocation("dicom_folder_location_1");

        CtScan ctScan2 = new CtScan();
        ctScan2.setFolderLocation("old_folder_1");
        ctScan2.setNewFolderPath("new_folder_path_2");
        ctScan2.setDicomFolderLocation("dicom_folder_location_2");

        List<CtScan> ctScans = new ArrayList<>();
        ctScans.add(ctScan1);
        ctScans.add(ctScan2);

        // when
        boolean returnedResult = ctScanCollectionValidator.areAllFoldersUniqueInCollection(ctScans);

        // then
        assertEquals(false, returnedResult);

    }


    @Test
    void NewFolderPathNotUnique() {

        // given

        CtScan ctScan1 = new CtScan();
        ctScan1.setFolderLocation("old_folder_1");
        ctScan1.setNewFolderPath("new_folder_path_1");
        ctScan1.setDicomFolderLocation("dicom_folder_location_1");

        CtScan ctScan2 = new CtScan();
        ctScan2.setFolderLocation("old_folder_2");
        ctScan2.setNewFolderPath("new_folder_path_1");
        ctScan2.setDicomFolderLocation("dicom_folder_location_2");

        List<CtScan> ctScans = new ArrayList<>();
        ctScans.add(ctScan1);
        ctScans.add(ctScan2);

        // when
        boolean returnedResult = ctScanCollectionValidator.areAllFoldersUniqueInCollection(ctScans);

        // then
        assertEquals(false, returnedResult);

    }


    @Test
    void dicomFolderNotUnique() {

        // given

        CtScan ctScan1 = new CtScan();
        ctScan1.setFolderLocation("old_folder_1");
        ctScan1.setNewFolderPath("new_folder_path_1");
        ctScan1.setDicomFolderLocation("dicom_folder_location_1");

        CtScan ctScan2 = new CtScan();
        ctScan2.setFolderLocation("old_folder_2");
        ctScan2.setNewFolderPath("new_folder_path_2");
        ctScan2.setDicomFolderLocation("dicom_folder_location_1");

        List<CtScan> ctScans = new ArrayList<>();
        ctScans.add(ctScan1);
        ctScans.add(ctScan2);

        // when
        boolean returnedResult = ctScanCollectionValidator.areAllFoldersUniqueInCollection(ctScans);

        // then
        assertEquals(false, returnedResult);

    }




}





