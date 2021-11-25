package com.arilab.domain.validator.error;

import java.util.ArrayList;
import java.util.List;

public class CtScanErrorModel implements ErrorModel {

    private final String scanId;
    private List<String> errors = new ArrayList<>();


    public CtScanErrorModel(String scanId) {
        this.scanId = scanId;
    }

    @Override
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public void addError(String error) {
        errors.add(error);
    }

    @Override
    public String[] printErrors() {
        String[] output = new String[errors.size()];
        for (int i=0 ; i<errors.size(); i++) {
            output[i] = "CtScan: " + scanId + "has the error: " + errors.get(i);
        }
        return output;
    }
}
