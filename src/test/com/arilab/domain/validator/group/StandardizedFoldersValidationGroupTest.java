package com.arilab.domain.validator.group;

import com.arilab.service.CTScanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StandardizedFoldersValidationGroupTest {

    @Mock
    CTScanService ctScanService;

    @InjectMocks
    StandardizedFoldersValidationGroup standardizedFoldersValidationGroup;

    @Test
    void getValidatorsShouldReturnCorrectNumberOfValidators() {
        // given
        int numberOfValidatorsInGroup = 2;

        // then
        assertEquals(numberOfValidatorsInGroup, standardizedFoldersValidationGroup.getValidators().size());
    }
}