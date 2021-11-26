package com.arilab.domain.validator.group;

import com.arilab.domain.validator.CtScanCollectionValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AllFoldersUniqueValidationGroupTest {



    @Test
    void getValidatorsShouldReturnCorrectNumberOfValidators() {
        // given
        int numberOfValidatorsInGroup = 1;

        // when
        AllFoldersUniqueValidationGroup allFoldersUniqueValidationGroup = new AllFoldersUniqueValidationGroup();

        // then
        assertEquals(numberOfValidatorsInGroup, allFoldersUniqueValidationGroup.getValidators().size());
    }
}