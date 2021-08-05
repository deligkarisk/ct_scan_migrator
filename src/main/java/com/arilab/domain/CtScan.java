package com.arilab.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.ToString;

@ToString
public class CtScan {
    @CsvBindByName(column = "specimen_code")
    private  String specimenCode;

    @CsvBindByName(column = "ct_scan_note")
    private String ctScanNote;

    @CsvBindByName(column = "ethanol_conc")
    private String ethanolConcentration;

    @CsvBindByName(column = "ethanol_correct")
    private Boolean ethanolConcIsCorrect;

    @CsvBindByName(column = "wet")
    private String wet;

    @CsvBindByName(column = "body_part")
    private String bodyPart;

    @CsvBindByName(column = "special_identifier")
    private String specialIdentifier;

    @CsvBindByName(column = "folder_location")
    private String folderLocation;

    @CsvBindByName(column = "folder_location_exists")
    private Boolean folderLocationExists;

    @CsvBindByName(column = "dicom_folder_location")
    private String dicomFolderLocation;

    @CsvBindByName(column = "scan_user")
    private String scanUser;

    @CsvBindByName(column = "scan_date")
    private String scanDate;

    @CsvBindByName(column = "scanDateCorrect")
    private Boolean scanDateCorrect;

    @CsvBindByName(column = "staining")
    private String staining;

    @CsvBindByName(column = "staining_correct")
    private Boolean stainingIsCorrect;

    @CsvBindByName(column = "antscan")
    private String antscan;

    @CsvBindByName(column = "antscan_code")
    private String antscanCode;

    @CsvBindByName(column = "antscanCorrect")
    private Boolean antscanCodingIsCorrect;

    @CsvBindByName(column = "model")
    private String model;

    @CsvBindByName(column = "model_correct")
    private Boolean modelIsCorrect;

    @CsvBindByName(column = "dry_method")
    private String dryMethod;

    @CsvBindByName(column = "scan_reason")
    private String scanReason;

    @CsvBindByName(column = "prep_note")
    private String prepNote;

    @CsvBindByName(column = "specimen_code_exists")
    private Boolean specimenCodeExists;

    @CsvBindByName(column = "wet_dry_comb_correct")
    private Boolean wetDryCombinationIsCorrect;

    @CsvBindByName(column = "dry_method_correct")
    private Boolean dryMethodIsCorrect;

    @CsvBindByName(column = "body_part_correct")
    private Boolean bodypartIsCorrect;

    @CsvBindByName(column = "input_data_valid")
    private Boolean AllinputDataIsValid;

    @CsvBindByName(column = "derived_data_valid")
    private Boolean derivedDataIsValid;

    @CsvBindByName(column = "new_folder_path")
    private String newFolderPath;

    @CsvBindByName(column = "new_folder_available_on_bucket")
    private Boolean newFolderPathAvailable;

    @CsvBindByName(column = "new_folder_available_in_db")
    private Boolean newFolderPathAvailableIntheDatabase;

    @CsvBindByName(column = "timestamp")
    private String timestamp;

    @CsvBindByName(column = "migrated")
    private Boolean migrated;

    @CsvBindByName(column = "migration_exception")
    private String migrationException;

    public Boolean getMigrated() {
        return migrated;
    }

    public void setMigrated(Boolean migrated) {
        this.migrated = migrated;
    }


    public String getMigrationException() {
        return migrationException;
    }

    public void setMigrationException(String migrationException) {
        this.migrationException = migrationException;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getSpecimenCodeExists() {
        return specimenCodeExists;
    }

    public void setSpecimenCodeExists(Boolean specimenCodeExists) {
        this.specimenCodeExists = specimenCodeExists;
    }

    public Boolean getWetDryCombinationIsCorrect() {
        return wetDryCombinationIsCorrect;
    }

    public void setWetDryCombinationIsCorrect(Boolean wetDryCombinationIsCorrect) {
        this.wetDryCombinationIsCorrect = wetDryCombinationIsCorrect;
    }

    public String getNewFolderPath() {
        return newFolderPath;
    }

    public void setNewFolderPath(String newFolderPath) {
        this.newFolderPath = newFolderPath;
    }


    public String getSpecimenCode() {
        return specimenCode;
    }

    public void setSpecimenCode(String specimenCode) {
        this.specimenCode = specimenCode;
    }

    public String getCtScanNote() {
        return ctScanNote;
    }

    public void setCtScanNote(String ctScanNote) {
        this.ctScanNote = ctScanNote;
    }

    public String getEthanolConcentration() {
        return ethanolConcentration;
    }

    public void setEthanolConcentration(String ethanolConcentration) {
        this.ethanolConcentration = ethanolConcentration;
    }

    public String getWet() {
        return wet;
    }

    public void setWet(String wet) {
        this.wet = wet;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getSpecialIdentifier() {
        return specialIdentifier;
    }

    public void setSpecialIdentifier(String specialIdentifier) {
        this.specialIdentifier = specialIdentifier;
    }

    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
    }

    public String getScanUser() {
        return scanUser;
    }

    public void setScanUser(String scanUser) {
        this.scanUser = scanUser;
    }

    public String getStaining() {
        return staining;
    }

    public void setStaining(String staining) {
        this.staining = staining;
    }

    public String getAntscan() {
        return antscan;
    }

    public void setAntscan(String antscan) {
        this.antscan = antscan;
    }

    public String getAntscanCode() {
        return antscanCode;
    }

    public void setAntscanCode(String antscanCode) {
        this.antscanCode = antscanCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDryMethod() {
        return dryMethod;
    }

    public void setDryMethod(String dryMethod) {
        this.dryMethod = dryMethod;
    }

    public String getScanReason() {
        return scanReason;
    }

    public void setScanReason(String scanReason) {
        this.scanReason = scanReason;
    }

    public String getPrepNote() {
        return prepNote;
    }

    public void setPrepNote(String prepNote) {
        this.prepNote = prepNote;
    }

    public Boolean getDryMethodIsCorrect() {
        return dryMethodIsCorrect;
    }

    public void setDryMethodIsCorrect(Boolean dryMethodIsCorrect) {
        this.dryMethodIsCorrect = dryMethodIsCorrect;
    }

    public Boolean getBodypartIsCorrect() {
        return bodypartIsCorrect;
    }

    public void setBodypartIsCorrect(Boolean bodypartIsCorrect) {
        this.bodypartIsCorrect = bodypartIsCorrect;
    }

    public Boolean getAllinputDataIsValid() {
        return AllinputDataIsValid;
    }

    public void setAllinputDataIsValid(Boolean allinputDataIsValid) {
        this.AllinputDataIsValid = allinputDataIsValid;
    }

    public String getDicomFolderLocation() {
        return dicomFolderLocation;
    }

    public void setDicomFolderLocation(String dicomFolderLocation) {
        this.dicomFolderLocation = dicomFolderLocation;
    }

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public Boolean getDerivedDataIsValid() {
        return derivedDataIsValid;
    }

    public void setDerivedDataIsValid(Boolean derivedDataIsValid) {
        this.derivedDataIsValid = derivedDataIsValid;
    }

    public Boolean getNewFolderPathAvailable() {
        return newFolderPathAvailable;
    }

    public void setNewFolderPathAvailable(Boolean newFolderPathAvailable) {
        this.newFolderPathAvailable = newFolderPathAvailable;
    }

    public Boolean getFolderLocationExists() {
        return folderLocationExists;
    }

    public void setFolderLocationExists(Boolean folderLocationExists) {
        this.folderLocationExists = folderLocationExists;
    }

    public Boolean getModelIsCorrect() {
        return modelIsCorrect;
    }

    public void setModelIsCorrect(Boolean modelIsCorrect) {
        this.modelIsCorrect = modelIsCorrect;
    }

    public Boolean getEthanolConcIsCorrect() {
        return ethanolConcIsCorrect;
    }

    public void setEthanolConcIsCorrect(Boolean ethanolConcIsCorrect) {
        this.ethanolConcIsCorrect = ethanolConcIsCorrect;
    }

    public Boolean getStainingIsCorrect() {
        return stainingIsCorrect;
    }

    public void setStainingIsCorrect(Boolean stainingIsCorrect) {
        this.stainingIsCorrect = stainingIsCorrect;
    }

    public Boolean getAntscanCodingIsCorrect() {
        return antscanCodingIsCorrect;
    }

    public void setAntscanCodingIsCorrect(Boolean antscanCodingIsCorrect) {
        this.antscanCodingIsCorrect = antscanCodingIsCorrect;
    }

    public Boolean getNewFolderPathAvailableIntheDatabase() {
        return newFolderPathAvailableIntheDatabase;
    }

    public void setNewFolderPathAvailableIntheDatabase(Boolean newFolderPathAvailableIntheDatabase) {
        this.newFolderPathAvailableIntheDatabase = newFolderPathAvailableIntheDatabase;
    }

    public Boolean getScanDateCorrect() {
        return scanDateCorrect;
    }

    public void setScanDateCorrect(Boolean scanDateCorrect) {
        this.scanDateCorrect = scanDateCorrect;
    }
}
