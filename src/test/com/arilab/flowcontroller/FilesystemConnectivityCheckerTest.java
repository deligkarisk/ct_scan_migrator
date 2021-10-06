package com.arilab.flowcontroller;

import com.arilab.system.SystemExit;
import com.arilab.utils.Config;
import com.arilab.utils.FileSystemUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilesystemConnectivityCheckerTest {

    @Mock
    FileSystemUtils fileSystemUtils;

    @Mock
    SystemExit systemExit;

    @Mock
    Config config;

    @InjectMocks
    FilesystemConnectivityChecker filesystemConnectivityChecker;

    @BeforeEach
    void setUp() {
        when(fileSystemUtils.stringToPath(any())).thenReturn(Paths.get(""));
    }

    @Test
    void checkExitIfNotMounted() {
        // given
        when(fileSystemUtils.isMounted(any())).thenReturn(false);

        // when
        filesystemConnectivityChecker.check();

        // then
        verify(systemExit).exit(1);
    }

    @Test
    void checkNotExitIfMounted() {
        // given
        when(fileSystemUtils.isMounted(any())).thenReturn(true);
        when(fileSystemUtils.isWriteable(any())).thenReturn(true);

        // when
        filesystemConnectivityChecker.check();

        // then
       verifyNoInteractions(systemExit);
    }
}