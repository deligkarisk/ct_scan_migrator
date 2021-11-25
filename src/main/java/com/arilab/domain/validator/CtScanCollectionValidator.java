package com.arilab.domain.validator;

import com.arilab.domain.CtScanCollection;

import java.util.Optional;

public interface CtScanCollectionValidator {

    public Optional<String> validate(CtScanCollection ctScanCollection);
}
