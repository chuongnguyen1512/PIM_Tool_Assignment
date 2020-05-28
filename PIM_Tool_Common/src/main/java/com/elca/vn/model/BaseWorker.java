package com.elca.vn.model;

import javafx.concurrent.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseWorker extends Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseWorker.class);

    @Override
    protected void succeeded() {
        LOGGER.info("Done background process");
    }

    @Override
    protected void failed() {
        LOGGER.info("Background process are facinge an error");
    }

    @Override
    protected void running() {
        LOGGER.info("background process is running");
    }
}
