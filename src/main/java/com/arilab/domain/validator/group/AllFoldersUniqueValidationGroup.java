package com.arilab.domain.validator.group;

import com.arilab.domain.validator.AllFoldersInCollectionUniqueValidator;
import com.arilab.domain.validator.CtScanCollectionValidator;
import com.arilab.domain.validator.CtScanValidator;

import java.util.ArrayList;

public class AllFoldersUniqueValidationGroup extends ValidatorGroup<CtScanCollectionValidator> {


    public AllFoldersUniqueValidationGroup() {
        validators.add(new AllFoldersInCollectionUniqueValidator());

    }
}
