package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import com.arilab.domain.CtScanCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AllFoldersInCollectionUniqueValidatorTest {

    AllFoldersInCollectionUniqueValidator allFoldersInCollectionUniqueValidator =
            new AllFoldersInCollectionUniqueValidator();

    @Test
    void validateNoErrorMessageWhenFoldersUnique() {
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
        CtScanCollection ctScanCollection = new CtScanCollection(ctScans);

        // when
        Optional<String> returnedResult = allFoldersInCollectionUniqueValidator.validate(ctScanCollection);

        // then
        assertEquals(Optional.empty(), returnedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "old_folder_1, new_folder_1, dicom_folder_1, old_folder_1, new_folder_2, dicom_folder_2",
            "old_folder_1, new_folder_1, dicom_folder_1, old_folder_2, new_folder_1, dicom_folder_2",
            "old_folder_1, new_folder_1, dicom_folder_1, old_folder_2, new_folder_2, dicom_folder_1"
    })
    void validateErrorMessageExistsWhenFolderLocationNotUnique(String oldFolder1, String newFolder1, String dicomFolder1, String oldFolder2,
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
        CtScanCollection ctScanCollection = new CtScanCollection(ctScans);


        // when
        Optional<String> returnedResult = allFoldersInCollectionUniqueValidator.validate(ctScanCollection);

        // then
        assertEquals(Optional.of("Not all folders in the dataset are unique."), returnedResult);
    }
}