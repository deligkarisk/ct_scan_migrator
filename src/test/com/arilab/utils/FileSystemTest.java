package com.arilab.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileSystemTest {

    private static final String PROPERTIES_FILE = "./config.properties";


    @Mock
    Config config;

    @InjectMocks
    FileSystem fileSystem;



    @Test()
    void throwExceptionIfNotMounted() {
        when(config.getSourceDirectory()).thenReturn("unmountedFolderHere");
        when(config.getTargetDirectory()).thenReturn("anotherUnmountedFolderHere");

        assertThrows(IOException.class, () -> {
            fileSystem.filesystemCheck();
        });
    }

    @Test
    void noExceptionsIfMountedAndWriteable(@TempDir File sourceTempDir, @TempDir File targetSourceDir) {
        when(config.getSourceDirectory()).thenReturn(sourceTempDir.toString());
        when(config.getTargetDirectory()).thenReturn(targetSourceDir.toString());
        try {
            fileSystem.filesystemCheck();
        } catch (IOException e) {
            fail("Exception caught but no exception was expected.");
        }
    }

    @Test
    void throwExceptionIfNotWriteable(@TempDir File sourceTempDir, @TempDir File targetSourceDir) {
        sourceTempDir.setReadOnly();
        targetSourceDir.setReadOnly();

        when(config.getSourceDirectory()).thenReturn(sourceTempDir.toString());
        when(config.getTargetDirectory()).thenReturn(targetSourceDir.toString());

        assertThrows(IOException.class, () -> {fileSystem.filesystemCheck(); });
    }
}