package com.arilab.utils;

import com.arilab.domain.CtScan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    FileUtils fileUtils;
    DirectoryMoverSpy directoryMoverSpy;

    @BeforeEach
    void setUp() {
        directoryMoverSpy = new DirectoryMoverSpy();
        fileUtils = new FileUtils(directoryMoverSpy);

    }

    @Test
    void moveMainFolderDummy() throws IOException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setFolderLocation("oldFolderLocation");
        ctScan.setNewFolderPath("newFolderLocation");

        // when
        fileUtils.moveMainFolder(ctScan,true );

        // then
        assertEquals(null, directoryMoverSpy.calledSourceDir);
        assertEquals(null, directoryMoverSpy.calledDestDir);
    }


    @Test
    void moveMainFolderNotDummy() throws IOException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setFolderLocation("oldFolderLocation");
        ctScan.setNewFolderPath("newFolderLocation");

        // when
        fileUtils.moveMainFolder(ctScan,false );

        // then
        assertEquals("oldFolderLocation", directoryMoverSpy.calledSourceDir.toString());
        assertEquals("newFolderLocation", directoryMoverSpy.calledDestDir.toString());
    }

    @Test
    void moveDicomFolderNotDummy() throws IOException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation("dicomFolder/folderA");
        ctScan.setNewDicomFolderPath("newDicomeFolder/folderB");

        // when
        fileUtils.moveDicomFolder(ctScan, false);

        // then
        assertEquals("dicomFolder/folderA", directoryMoverSpy.calledSourceDir.toString());
        assertEquals("newDicomeFolder/folderB", directoryMoverSpy.calledDestDir.toString());
    }



    @Test
    void moveDicomFolderDummy() throws IOException {
        // given
        CtScan ctScan = new CtScan();
        ctScan.setDicomFolderLocation("dicomFolder/folderA");
        ctScan.setNewDicomFolderPath("newDicomeFolder/folderB");

        // when
        fileUtils.moveDicomFolder(ctScan, true);

        // then
        assertNull(directoryMoverSpy.calledSourceDir);
        assertNull(directoryMoverSpy.calledDestDir);

    }
}