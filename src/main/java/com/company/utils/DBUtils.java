package com.company.utils;


import org.apache.log4j.Logger;

import java.io.IOException;

public class DBUtils {
    private final static Logger logger = Logger.getLogger(DBUtils.class);

    public static void connectToDB() {
        logger.info("Creating DB connection...");
        logger.info("Project root is "+System.getProperty("user.dir"));
        String command = System.getProperty("user.dir")+"\\gradlew.bat startDatabase";
        try {
            Runtime.getRuntime().exec(command);
            logger.info("DB Connection successful...");
        } catch (IOException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

    }

    public static void stopDBConnection() {
        logger.info("Stopping DB connection...");
        String command = System.getProperty("user.dir")+"\\gradlew.bat stopDatabase";
        try {
            Runtime.getRuntime().exec(command);
            logger.info("stopDatabase successful...");
        } catch (IOException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }

    }

}