package com.arilab.domain.validator.group;

import com.arilab.domain.validator.*;
import com.arilab.service.DatabaseService;

import java.util.ArrayList;

public class BasicFieldValidationGroup extends ValidatorGroup<CtScanValidator> {

    DatabaseService databaseService;



    public BasicFieldValidationGroup(DatabaseService databaseService) {
        this.databaseService = databaseService;

        SpecimenCodeExistsCtScanValidator specimenCodeExistsValidator = new SpecimenCodeExistsCtScanValidator(databaseService);
        WetDryCombinationCtScanValidator wetDryCombinationValidator = new WetDryCombinationCtScanValidator();
        DryMethodCtScanValidator dryMethodValidator = new DryMethodCtScanValidator();
        BodyPartCtScanValidator bodyPartValidator = new BodyPartCtScanValidator();
        FolderLocationExistsCtScanValidator folderLocationExistsValidator = new FolderLocationExistsCtScanValidator();
        DicomFolderLocationExistsCtScanValidator dicomFolderLocationExistsValidator = new DicomFolderLocationExistsCtScanValidator();
        ModelCtScanValidator modelValidator = new ModelCtScanValidator();
        StainingCtScanValidator stainingValidator = new StainingCtScanValidator();
        EthanolConcCtScanValidator ethanolConcValidator = new EthanolConcCtScanValidator();
        AntscanCodingCtScanValidator antscanCodingValidator = new AntscanCodingCtScanValidator();
        DicomFolderNotInMainFolderCtScanValidator dicomFolderNotInMainFolderValidator = new DicomFolderNotInMainFolderCtScanValidator();

        validators.add(specimenCodeExistsValidator);
        validators.add(wetDryCombinationValidator);
        validators.add(dryMethodValidator);
        validators.add(bodyPartValidator);
        validators.add(folderLocationExistsValidator);
        validators.add(dicomFolderLocationExistsValidator);
        validators.add(modelValidator);
        validators.add(stainingValidator);
        validators.add(ethanolConcValidator);
        validators.add(antscanCodingValidator);
        validators.add(dicomFolderNotInMainFolderValidator);
    }
}
