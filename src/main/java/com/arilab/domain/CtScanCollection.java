package com.arilab.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;


public class CtScanCollection {

    private static Logger logger = LoggerFactory.getLogger(CtScanCollection.class);

    private List<CtScan> ctScans;

    public CtScanCollection(List<CtScan> ctScans) {
        this.ctScans = ctScans;
    }

    public List<CtScan> getCtScans() {
        return ctScans;
    }
}

