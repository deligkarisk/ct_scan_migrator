package com.arilab.domain.validator.group;

import com.arilab.domain.validator.CtScanValidator;

import java.util.ArrayList;
import java.util.List;

public abstract class ValidatorGroup<T> {

    protected ArrayList<T> validators = new ArrayList<>();

    public final List<T> getValidators() {
        return List.copyOf(validators);
    }
}
