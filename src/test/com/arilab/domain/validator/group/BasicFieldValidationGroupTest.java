package com.arilab.domain.validator.group;

import com.arilab.service.DatabaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BasicFieldValidationGroupTest {

    @Mock
    DatabaseService databaseService;

    @InjectMocks
    BasicFieldValidationGroup basicFieldValidationGroup;

    @Test
    void getValidatorsShouldReturnCorrectNumberOfValidators() {
        // given
        int numberOfValidatorsInGroup = 11;

        // then
        assertEquals(numberOfValidatorsInGroup, basicFieldValidationGroup.getValidators().size());
    }
}