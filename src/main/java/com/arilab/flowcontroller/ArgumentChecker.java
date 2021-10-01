package com.arilab.flowcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgumentChecker {

    public Boolean check(String[] arguments) {

        Logger logger = LoggerFactory.getLogger(ArgumentChecker.class);

        if (arguments.length < 2) {
            logger.error("The minimum number of arguments was not entered.");
            throw new RuntimeException("The minimum number of arguments was not entered.");
        }

        if (arguments.length == 3) {
            if (!arguments[2].equals("--do-migration")) {
                logger.error("Erroneous third argument");
                throw new RuntimeException("Erroneous third argument");
            }


        }

        return Boolean.TRUE;

    }
}
