package com.arilab.utils;

import com.arilab.domain.CTScan;

import java.util.Iterator;
import java.util.List;

public class CTScanUtilsService {

    CTScanUtils ctScanUtils = new CTScanUtils();


    public void fixScans(List scanList) {
        Iterator<CTScan> ctScanIterator = scanList.iterator();
        while (ctScanIterator.hasNext()) {
            CTScan ctScan = ctScanIterator.next();
            ctScanUtils.fixScanFolderLocations(ctScan);
            ctScanUtils.findDicomFolderLocation(ctScan, 1, "_OUT");
            ctScanUtils.findScanCreatedDate(ctScan);
            ctScanUtils.setTimestampOnCTScan(ctScan);
        }
    }



    public  void findNewFolderNames(List scanList) {
        Iterator<CTScan> ctScanIterator = scanList.iterator();
        while (ctScanIterator.hasNext()) {
            CTScan ctScan = ctScanIterator.next();
            ctScanUtils.findNewFolderName(ctScan);
        }
    }


}
