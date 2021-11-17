package com.arilab.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryMoverProperTest {

    DirectoryMoverProper directoryMoverProper = new DirectoryMoverProper();

    @Test
    void move(@TempDir File sourceParentDir, @TempDir File destParentDir) throws IOException {
        // given
        String fileName = "testFile.txt";
        String folderNameOriginal = "CASENT000_202100_190000";
        String folderNameStandardized = "CASENT000_Head_SpecialID_202100_190000";

        Path sourcePath = Files.createDirectory(Paths.get(sourceParentDir.toString(), folderNameOriginal));
        Path fileInSourcePath = Files.createFile(Paths.get(sourcePath.toString(), fileName));

        Path destPath = Paths.get(destParentDir.toString(), folderNameStandardized);
        Path fileInDestPath = Paths.get(destPath.toString(), fileName);
        assertFalse(Files.exists(destPath)); // Before doing the migration let's verify the folder does not actually
        // exist.

        // when
        directoryMoverProper.move(sourcePath.toFile(), destPath.toFile());

        // then
        assertTrue(Files.exists(destPath));
        assertTrue(Files.exists(fileInDestPath));
        assertFalse(Files.exists(fileInSourcePath));
        assertFalse(Files.exists(sourcePath));
    }
}