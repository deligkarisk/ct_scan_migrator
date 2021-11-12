package com.arilab.utils;

import com.arilab.domain.CtScan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    FileUtils fileUtils;
    FakeDirectoryMover fakeDirectoryMover;

    @BeforeEach
    void setUp() {
        fakeDirectoryMover = new FakeDirectoryMover();
        fileUtils = new FileUtils(fakeDirectoryMover);

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
        assertEquals(null, fakeDirectoryMover.calledSourceDir);
        assertEquals(null, fakeDirectoryMover.calledDestDir);
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
        assertEquals("oldFolderLocation", fakeDirectoryMover.calledSourceDir.toString());
        assertEquals("newFolderLocation", fakeDirectoryMover.calledDestDir.toString());
    }
}