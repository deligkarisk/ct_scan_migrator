package com.arilab.domain.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationGroup1 extends ValidatorGroup {

    SpecimenCodeExists specimenCodeExists = new SpecimenCodeExists();

    ArrayList<Validator> validators = new ArrayList<>();

    public ValidationGroup1() {
        validators.add(specimenCodeExists);
    }

}
