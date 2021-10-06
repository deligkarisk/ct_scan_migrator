package com.arilab.flowcontroller;

import com.arilab.service.DatabaseService;
import com.arilab.system.SystemExit;
import com.arilab.utils.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseConnectivityCheckerTest {

    @Mock
    SystemExit systemExit;

    @Mock
    Config config;

    @Mock
    DatabaseService databaseService;


    @InjectMocks
    DatabaseConnectivityChecker databaseConnectivityChecker;


    @BeforeEach
    void setUp() {

    }

    @Test
    void exitIfDbNotAccessible() throws SQLException {

        // given
        when(databaseService.specimenCodeExists("Test")).thenThrow(SQLException.class);

        // when
        databaseConnectivityChecker.check();

        // then
        verify(systemExit).exit(1);
    }

    @Test
    void doNotExitIfDbIsAccessible() throws SQLException {

        // given
        when(databaseService.specimenCodeExists("Test")).thenReturn(true);

        // when
        databaseConnectivityChecker.check();

        // then
        verifyNoInteractions(systemExit);
    }
}