package com.arilab.domain;

import com.arilab.service.DatabaseService;
import com.arilab.utils.PathUtils;
import org.checkerframework.checker.fenum.qual.AwtAlphaCompositingRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    void dicomFolderLocationExistsLocationEmpty() {
        CtScan ctScan = new CtScan();
        Boolean result = ctScanValidator.dicomFolderLocationExists(ctScan);
        assertEquals(true, result);
    }


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

    @ParameterizedTest
    @CsvSource({
            "Yes, ANTSCANCODE",
            "No,"
    })
    void antscanIsCorrectValid(String antscanFlag, String antscanCode) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setAntscan(antscanFlag);
        ctScan.setAntscanCode(antscanCode);

        // when
        Boolean returnedResult = ctScanValidator.antscanIsCorrect(ctScan);

        // then
        assertEquals(true, returnedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "No, ANTSCANCODE",
            "Yes,"
    })
    void antscanIsCorrectInvalid(String antscanFlag, String antscanCode) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setAntscan(antscanFlag);
        ctScan.setAntscanCode(antscanCode);

        // when
        Boolean returnedResult = ctScanValidator.antscanIsCorrect(ctScan);

        // then
        assertEquals(false, returnedResult);
    }



    @ParameterizedTest
    @CsvSource({
            "Yes,,EthanolConc",
            "No,DryMethod,No ethanol used"
    })
    void wetDryCombinationIsCorrectValid(String wetFlag, String dryFlag, String ethanolConcentrationFlag) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setWet(wetFlag);
        ctScan.setDryMethod(dryFlag);
        ctScan.setEthanolConcentration(ethanolConcentrationFlag);

        // when
        Boolean returnedValue = ctScanValidator.wetDryCombinationIsCorrect(ctScan);

        // then
        assertEquals(true,returnedValue);
    }



    @ParameterizedTest
    @CsvSource({
            "Yes,DryMethod,EthanolConc",
            "No,DryMethod,EthanolConc",
            "Yes, DryMethod,",
            "Yes,,",
            "No,,",
            "No,DryMethod,EthanolConc"
    })
    void wetDryCombinationIsCorrectInvalid(String wetFlag, String dryFlag, String ethanolConcentrationFlag) {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setWet(wetFlag);
        ctScan.setDryMethod(dryFlag);
        ctScan.setEthanolConcentration(ethanolConcentrationFlag);

        // when
        Boolean returnedValue = ctScanValidator.wetDryCombinationIsCorrect(ctScan);

        // then
        assertEquals(false,returnedValue);
    }

}