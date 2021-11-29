package com.arilab.domain.validator.error;

import java.util.List;

public interface ErrorModel {

    boolean hasErrors();
    void addError(String error);
    List<String> printFormattedErrors();
    List<String> getErrors();

    int getNumberOfErrors();
}
