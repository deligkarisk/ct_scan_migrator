package com.arilab.domain.validator;

import com.arilab.domain.CtScan;

import java.util.Optional;

public interface Validator {

    public Optional<String> validate(CtScan ctScan);

}