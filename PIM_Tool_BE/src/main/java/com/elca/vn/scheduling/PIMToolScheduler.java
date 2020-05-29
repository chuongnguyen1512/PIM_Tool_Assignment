package com.elca.vn.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PIMToolScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PIMToolScheduler.class);

    @Scheduled(fixedDelay = 10000)
    public void schedulingTask() {
        LOGGER.info("PIM Service is running");
    }
}
