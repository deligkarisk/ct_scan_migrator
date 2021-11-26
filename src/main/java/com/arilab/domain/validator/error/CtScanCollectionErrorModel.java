package com.arilab.domain.validator.error;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CtScanCollectionErrorModel implements ErrorModel {

    private List<String> errors = new ArrayList<>();


    @Override
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public void addError(String error) {
        errors.add(error);
    }

    @Override
    public List<String> printErrors() {
        List<String> errorsToPrint = new LinkedList<>();
        for (int i=0 ; i<errors.size(); i++) {
            errorsToPrint.add("CtScan collection has the error: " + errors.get(i));
        }
        return errorsToPrint;
    }



    @Override
    public int getNumberOfErrors() {
        return errors.size();
    }
}
