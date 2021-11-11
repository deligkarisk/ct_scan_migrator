package com.arilab.flowcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.arilab.Main.systemExit;

public class UniqueFoldersChecker {

    private static Logger logger = LoggerFactory.getLogger(UniqueFoldersChecker.class);

    public void check(Boolean allFoldersUnique) {
        if (!allFoldersUnique) {
            logger.error("Not all folders unique, system will exit.");
            systemExit.exit(1);
            // todo: write bean file before exiting?
        }

    }
}
