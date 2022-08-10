package com.github.jacobbrewer1.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {

    private final Logger logger;

    public Logging(Logger logger) {
        this.logger = logger;
    }

    public void logInfo(String message) {
        logger.log(Level.INFO, message);
    }
}
