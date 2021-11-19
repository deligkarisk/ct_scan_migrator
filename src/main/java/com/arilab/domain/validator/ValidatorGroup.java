package com.arilab.domain.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ValidatorGroup {

    private ArrayList<Validator> validators = new ArrayList<>();

    public final List<Validator> getValidators() {
        return List.copyOf(validators);
    }
}
