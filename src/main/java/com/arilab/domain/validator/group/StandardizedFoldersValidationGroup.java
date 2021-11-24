package com.arilab.domain.validator.group;

import com.arilab.domain.validator.StandardizedPathAvailableInDB;
import com.arilab.domain.validator.StandardizedPathAvailableInFilesystem;
import com.arilab.domain.validator.Validator;
import com.arilab.service.CTScanService;

import java.util.ArrayList;

public class StandardizedFoldersValidationGroup extends ValidatorGroup {

    CTScanService ctScanService;

    ArrayList<Validator> validators = new ArrayList<>();


    public StandardizedFoldersValidationGroup(CTScanService ctScanService) {
        this.ctScanService = ctScanService;

        validators.add(new StandardizedPathAvailableInFilesystem());
        validators.add(new StandardizedPathAvailableInDB(ctScanService));


    }
}
