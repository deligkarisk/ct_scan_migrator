package com.arilab.utils;

public class StringUtils {

    public String mergeStrings(String prepend, String label) {
        return("./" + prepend + "_" + label + ".csv");
    }
}
