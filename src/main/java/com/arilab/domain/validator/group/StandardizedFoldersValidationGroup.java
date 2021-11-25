package com.arilab.domain.validator.group;

import com.arilab.domain.validator.StandardizedPathAvailableInDB;
import com.arilab.domain.validator.StandardizedPathAvailableInFilesystem;
import com.arilab.domain.validator.CtScanValidator;
import com.arilab.service.CTScanService;

public class StandardizedFoldersValidationGroup extends ValidatorGroup<CtScanValidator> {

    CTScanService ctScanService;

    public StandardizedFoldersValidationGroup(CTScanService ctScanService) {
        this.ctScanService = ctScanService;

        validators.add(new StandardizedPathAvailableInFilesystem());
        validators.add(new StandardizedPathAvailableInDB(ctScanService));


    }
}
