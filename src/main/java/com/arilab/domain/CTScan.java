package com.arilab.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.ToString;

@ToString
public class CTScan {
    @CsvBindByName(column = "specimen_code")
    private  String specimenCode;

    @CsvBindByName(column = "ct_scan_note")
    private String ctScanNote;

    @CsvBindByName(column = "ethanol_conc")
    private String ethanolConcentration;

    @CsvBindByName(column = "wet")
    private String wet;

    @CsvBindByName(column = "body_part")
    private String bodyPart;

    @CsvBindByName(column = "special_identifier")
    private String specialIdentifier;

    @CsvBindByName(column = "folder_location")
    private String folderLocation;

    @CsvBindByName(column = "dicom_folder_location")
    private String dicomFolderLocation;

    @CsvBindByName(column = "scan_user")
    private String scanUser;

    @CsvBindByName(column = "scan_date")
    private String scanDate;

    @CsvBindByName(column = "staining")
    private String staining;

    @CsvBindByName(column = "antscan")
    private String antscan;


    @CsvBindByName(column = "antscan_code")
    private String antscanCode;

    @CsvBindByName(column = "model")
    private String model;

    @CsvBindByName(column = "dry_method")
    private String dryMethod;

    @CsvBindByName(column = "scan_reason")
    private String scanReason;

    @CsvBindByName(column = "prep_note")
    private String prepNote;

    @CsvBindByName(column = "specimen_code_exists")
    private Boolean specimenCodeExists;

    @CsvBindByName(column = "scan_exists_in_bucket")
    private Boolean scanExistsInBucket;

    @CsvBindByName(column = "wet_dry_comb_correct")
    private Boolean wetDryCombinationIsCorrect;

    @CsvBindByName(column = "dry_method_correct")
    private Boolean dryMethodIsCorrect;

    @CsvBindByName(column = "body_part_correct")
    private Boolean bodypartIsCorrect;

    @CsvBindByName(column = "dicom_folder_exists")
    private Boolean dicomFolderExists;

    @CsvBindByName(column = "folder_creation_time")
    private String folderCreationTime;

    @CsvBindByName(column = "folder_creation_time_exists")
    private Boolean folderCreationTimeExists;

    @CsvBindByName(column = "can_migrate")
    private Boolean canMigrate;

    private String newFolderPath;

    public Boolean getSpecimenCodeExists() {
        return specimenCodeExists;
    }

    public void setSpecimenCodeExists(Boolean specimenCodeExists) {
        this.specimenCodeExists = specimenCodeExists;
    }

    public Boolean getScanExistsInBucket() {
        return scanExistsInBucket;
    }

    public void setScanExistsInBucket(Boolean scanExistsInBucket) {
        this.scanExistsInBucket = scanExistsInBucket;
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

    public Boolean getCanMigrate() {
        return canMigrate;
    }

    public void setCanMigrate(Boolean canMigrate) {
        this.canMigrate = canMigrate;
    }

    public String getDicomFolderLocation() {
        return dicomFolderLocation;
    }

    public void setDicomFolderLocation(String dicomFolderLocation) {
        this.dicomFolderLocation = dicomFolderLocation;
    }

    public Boolean getDicomFolderExists() {
        return dicomFolderExists;
    }

    public void setDicomFolderExists(Boolean dicomFolderExists) {
        this.dicomFolderExists = dicomFolderExists;
    }

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public String getFolderCreationTime() {
        return folderCreationTime;
    }

    public void setFolderCreationTime(String folderCreationTime) {
        this.folderCreationTime = folderCreationTime;
    }

    public Boolean getFolderCreationTimeExists() {
        return folderCreationTimeExists;
    }

    public void setFolderCreationTimeExists(Boolean folderCreationTimeExists) {
        this.folderCreationTimeExists = folderCreationTimeExists;
    }


}
