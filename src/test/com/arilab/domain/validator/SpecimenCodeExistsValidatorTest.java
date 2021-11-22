package com.arilab.domain.validator;

import com.arilab.domain.CtScan;
import com.arilab.service.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecimenCodeExistsValidatorTest {

    @Mock
    DatabaseService databaseService;

    @InjectMocks
    SpecimenCodeExistsValidator specimenCodeExistsValidator;

    CtScan ctScan;


    @BeforeEach
    void setUp() {
        ctScan = new CtScan();
        ctScan.setSpecimenCode("CASENT000000");
    }

    @Test
    void validateSpecimenExists() throws SQLException {
        // given
        when(databaseService.specimenCodeExists(any())).thenReturn(true);

        // when
        Optional<String> returnedResult = specimenCodeExistsValidator.validate(ctScan);

        // then
        assertEquals(Optional.empty(), returnedResult);
    }

    @Test
    void validateSpecimenDoesNotExist() throws SQLException {
        // given
        when(databaseService.specimenCodeExists(any())).thenReturn(false);

        // when
        Optional<String> returnedResult = specimenCodeExistsValidator.validate(ctScan);

        // then
        assertEquals(Optional.of("Specimen code does not exist in the database."), returnedResult);

    }
}