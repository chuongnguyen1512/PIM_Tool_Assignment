package com.elca.vn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;

@ComponentScan(basePackages = {
        "com.elca.vn",
        "com.elca.vn.repository",
        "com.elca.vn.config"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class PIMToolBEApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(PIMToolBEApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(PIMToolBEApplication.class, args);

        LOGGER.info("Host: {}", InetAddress.getLocalHost().getHostName());
    }
}