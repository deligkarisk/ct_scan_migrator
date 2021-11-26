package com.arilab.domain.validator.group;

import com.arilab.domain.validator.AllFoldersInCollectionUniqueValidator;
import com.arilab.domain.validator.CtScanCollectionValidator;

public class AllFoldersUniqueValidationGroup extends ValidatorGroup<CtScanCollectionValidator> {


    public AllFoldersUniqueValidationGroup() {
        validators.add(new AllFoldersInCollectionUniqueValidator());
    }
}

