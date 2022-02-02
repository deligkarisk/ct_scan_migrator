package com.arilab.utils;

import com.arilab.service.FileSystemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileSystemServiceTest {

    private static final String PROPERTIES_FILE = "./config.properties";


    @Mock
    Config config;

    @InjectMocks
    FileSystemService fileSystemService;



    @Test()
    void throwExceptionIfNotMounted() {
        when(config.getSourceDirectory()).thenReturn("unmountedFolderHere");
        when(config.getTargetDirectory()).thenReturn("anotherUnmountedFolderHere");

        assertThrows(IOException.class, () -> {
            fileSystemService.filesystemCheck();
        });
    }

    @Test
    void noExceptionsIfMountedAndWriteable(@TempDir File sourceTempDir, @TempDir File targetSourceDir) {
        when(config.getSourceDirectory()).thenReturn(sourceTempDir.toString());
        when(config.getTargetDirectory()).thenReturn(targetSourceDir.toString());
        try {
            fileSystemService.filesystemCheck();
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

        assertThrows(IOException.class, () -> {
            fileSystemService.filesystemCheck(); });
    }
}