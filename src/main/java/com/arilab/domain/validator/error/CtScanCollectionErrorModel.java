package com.arilab.domain.validator.error;

public class CtScanCollectionErrorModel implements ErrorModel {


    @Override
    public boolean hasErrors() {
        return false;
    }

    @Override
    public void addError(String error) {

    }

    @Override
    public String[] printErrors() {
        return new String[0];
    }
}
