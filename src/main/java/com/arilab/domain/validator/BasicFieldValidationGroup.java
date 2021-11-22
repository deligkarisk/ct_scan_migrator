package com.arilab.domain.validator;

import com.arilab.service.DatabaseService;

import java.util.ArrayList;

public class BasicFieldValidationGroup extends ValidatorGroup {

    DatabaseService databaseService;
    ArrayList<Validator> validators = new ArrayList<>();


    public BasicFieldValidationGroup(DatabaseService databaseService) {
        this.databaseService = databaseService;

        SpecimenCodeExistsValidator specimenCodeExistsValidator = new SpecimenCodeExistsValidator(databaseService);
        WetDryCombinationValidator wetDryCombinationValidator = new WetDryCombinationValidator();
        DryMethodValidator dryMethodValidator = new DryMethodValidator();
        BodyPartValidator bodyPartValidator = new BodyPartValidator();
        FolderLocationExistsValidator folderLocationExistsValidator = new FolderLocationExistsValidator();
        DicomFolderLocationExistsValidator dicomFolderLocationExistsValidator = new DicomFolderLocationExistsValidator();
        ModelValidator modelValidator = new ModelValidator();
        StainingValidator stainingValidator = new StainingValidator();
        EthanolConcValidator ethanolConcValidator = new EthanolConcValidator();
        AntscanCodingValidator antscanCodingValidator = new AntscanCodingValidator();
        DicomFolderNotInMainFolderValidator dicomFolderNotInMainFolderValidator = new DicomFolderNotInMainFolderValidator();

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
