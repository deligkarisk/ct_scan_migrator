package com.arilab.domain.validator.error;

public interface ErrorModel {

    boolean hasErrors();
    void addError(String error);
    String[] printErrors();
}
