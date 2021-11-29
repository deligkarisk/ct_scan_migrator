package com.arilab.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.ToString;

@ToString
public class CtScan {
    @CsvBindByName(column = "specimen_code", required = true)
    private String specimenCode;

    @CsvBindByName(column = "ct_scan_note")
    private String ctScanNote;


    @CsvBindByName(column = "ethanol_conc", required = true)
    private String ethanolConcentration;


    @CsvBindByName(column = "wet", required = true)
    private String wet;

    @CsvBindByName(column = "body_part", required = true)
    private String bodyPart;

    @CsvBindByName(column = "special_identifier")
    private String specialIdentifier;

    @CsvBindByName(column = "folder_location", required = true)
    private String folderLocation;

    @CsvBindByName(column = "dicom_folder_location")
    private String dicomFolderLocation;

    @CsvBindByName(column = "scan_user", required = true)
    private String scanUser;

    @CsvBindByName(column = "scan_date")
    private String scanDate;

    @CsvBindByName(column = "staining", required = true)
    private String staining;


    @CsvBindByName(column = "antscan", required = true)
    private String antscan;

    @CsvBindByName(column = "antscan_code")
    private String antscanCode;

    @CsvBindByName(column = "model", required = true)
    private String model;

    @CsvBindByName(column = "dry_method", required = true)
    private String dryMethod;

    @CsvBindByName(column = "scan_reason")
    private String scanReason;

    @CsvBindByName(column = "prep_note")
    private String prepNote;

    @CsvBindByName(column = "new_folder_path")
    private String newFolderPath;

    @CsvBindByName(column = "new_Dicom_folder_path")
    private String newDicomFolderPath;

    @CsvBindByName(column = "timestamp")
    private String timestamp;

    @CsvBindByName(column = "migrated")
    private Boolean migrated;

    @CsvBindByName(column = "migration_exception")
    private String migrationException;

    public CtScan(String specimenCode, String ethanolConcentration, String wet, String bodyPart,
                  String folderLocation, String staining, String antscan, String antscanCode, String model,
                  String dryMethod, String newFolderPath) {
        this.specimenCode = specimenCode;
        this.ethanolConcentration = ethanolConcentration;
        this.wet = wet;
        this.bodyPart = bodyPart;
        this.folderLocation = folderLocation;
        this.staining = staining;
        this.antscan = antscan;
        this.antscanCode = antscanCode;
        this.model = model;
        this.dryMethod = dryMethod;
        this.newFolderPath = newFolderPath;
    }

    public CtScan() {
    }

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

    public String getNewDicomFolderPath() {
        return newDicomFolderPath;
    }

    public void setNewDicomFolderPath(String newDicomFolderPath) {
        this.newDicomFolderPath = newDicomFolderPath;
    }
}
