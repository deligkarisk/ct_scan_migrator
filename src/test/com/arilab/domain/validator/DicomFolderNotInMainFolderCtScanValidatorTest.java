package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DicomFolderNotInMainFolderCtScanValidatorTest {

    DicomFolderNotInMainFolderCtScanValidator dicomFolderNotInMainFolderValidator = new DicomFolderNotInMainFolderCtScanValidator();

    @Test
    void validateDicomFolderNotInMainFolder(@TempDir File tempdir, @TempDir File tempdir2) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation(tempdir2.toString());
        ctScan.setFolderLocation(tempdir.toString());

        // when
        Optional<String> returnedResult = dicomFolderNotInMainFolderValidator.validate(ctScan);

        // then
        assertEquals(Optional.empty(), returnedResult);
    }


    @Test
    void validateDicomFolderInsideMainFolder(@TempDir File tempdir) {
        // given
        Path dicomPath = Paths.get(tempdir.toString(), "dicom");
        dicomPath.toFile().mkdir();

        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation(dicomPath.toString());
        ctScan.setFolderLocation(tempdir.toString());

        // when
        Optional<String> returnedResult = dicomFolderNotInMainFolderValidator.validate(ctScan);

        // then
        assertEquals(Optional.of("Dicom folder path entered is a child of main folder. Please remove the dicom folder " +
                "value."), returnedResult);
    }

    @Test
    void DicomFolderNull() {
        // given
        CtScan ctScan = new CtScan();

        // when
        Optional<String> returnedResult = dicomFolderNotInMainFolderValidator.validate(ctScan);

        // then
        assertEquals(Optional.empty(), returnedResult);
    }
}