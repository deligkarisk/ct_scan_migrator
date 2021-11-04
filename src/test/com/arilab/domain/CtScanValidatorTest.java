package com.arilab.domain;

import com.arilab.service.DatabaseService;
import com.arilab.utils.PathUtils;
import org.checkerframework.checker.fenum.qual.AwtAlphaCompositingRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CtScanValidatorTest {

    @Mock
    DatabaseService databaseService;

    @Mock
    PathUtils pathUtils;

    @InjectMocks
    CtScanValidator ctScanValidator;


    @Test
    void dicomFolderLocationExists(@TempDir Path tempdir) {
        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation(tempdir.toString());

        Boolean result = ctScanValidator.dicomFolderLocationExists(ctScan);
        assertEquals(true, result);
    }

    @Test
    void dicomFolderLocationDoesNotExist() {
        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation("randomPath");

        Boolean result = ctScanValidator.dicomFolderLocationExists(ctScan);
        assertEquals(false, result);
    }

    @Test
    void folderLocationExists(@TempDir Path tempdir) {
        CtScan ctScan = new CtScan();
        ctScan.setFolderLocation(tempdir.toString());

        Boolean result = ctScanValidator.folderLocationExists(ctScan);
        assertEquals(true, result);
    }


    @Test
    void folderLocationDoesNotExist() {
        CtScan ctScan = new CtScan();
        ctScan.setFolderLocation("randomPath");

        Boolean result = ctScanValidator.folderLocationExists(ctScan);
        assertEquals(false, result);
    }

    @Test
    void dicomFolderNotInMainFolderIsFalse(@TempDir File tempdir) {
        // given
        Path dicomPath = Paths.get(tempdir.toString(), "dicom");
        dicomPath.toFile().mkdir();

        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation(dicomPath.toString());
        ctScan.setFolderLocation(tempdir.toString());

        if (!ctScanValidator.dicomFolderLocationExists(ctScan)) {
            fail("dicom test folder, not exists, test cannot continue.");
        }

        // when
        Boolean result = ctScanValidator.dicomFolderNotInMainFolder(ctScan);

        // then
        assertEquals(false, result);
    }



    @Test
    void dicomFolderNotInMainFolderIsFalse(@TempDir File tempdir, @TempDir File tempdir2) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation(tempdir2.toString());
        ctScan.setFolderLocation(tempdir.toString());

        if (!ctScanValidator.dicomFolderLocationExists(ctScan)) {
            fail("dicom test folder, not exists, test cannot continue.");
        }

        // when
        Boolean result = ctScanValidator.dicomFolderNotInMainFolder(ctScan);

        // then
        assertEquals(true, result);
        System.out.println(tempdir);
        System.out.println(tempdir2);
    }

    @Test
    void antscanIsCorrect() {
    }

    @Test
    void stainingIsCorrect() {
    }

    @Test
    void ethanolConcIsCorrect() {
    }

    @Test
    void modelIsAnts() {
    }

    @Test
    void wetDryCombinationIsCorrect() {
    }

    @Test
    void dryMethodCheck() {
    }

    @Test
    void bodypartCheck() {
    }


    @Test
    void specimenCodeExists() {
    }

    @Test
    void allInputDataValidationsPassed() {
    }
    // todo: implement

}