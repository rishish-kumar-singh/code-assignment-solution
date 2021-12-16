package com.company;

import com.company.utils.DBUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    static {
        init();
    }

    private final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        //Below list will store all event objects read from log file
        List<Event> eventList = new ArrayList<>();

        //HashMap to store event duration per event id
        Map<String, Long> eventDurationMap = new HashMap<>();

        // Creating Object of ObjectMapper define in Jackson API
        ObjectMapper objectMapper = new ObjectMapper();


        /**
         The file absolute path needs to passed a command line arg. For example:
         gradle run --args "C:\\Users\\username\\solution\\src\\main\\resources\\logs\\logfile.txt"
         **/

        // Reading filepath from command line argument
        String filePath = args[0];
        logger.info("Reading data from the log file... " + filePath);

        //Reading logfile
        try {
            File file = new File(filePath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Event event = convertToObject(objectMapper, line);
                eventList.add(event);  // adding event objects to List
            }

            logger.info("Reading data file completed...");
        } catch (FileNotFoundException e) {
            logger.debug("File not found !" + e.getMessage());

        } catch (Exception e) {
            logger.debug(e.getMessage());
        }


        // Logic to calculate Event duration
        for (Event event : eventList) {
            if (eventDurationMap.get(event.getId()) == null) {
                eventDurationMap.put(event.getId(), event.getTimestamp());
            } else {
                //Since start and finish event can occur in any order
                long duration = Math.abs(eventDurationMap.get(event.getId()) - (event.getTimestamp()));
                eventDurationMap.put(event.getId(), duration);
            }
        }


        //Write the found event details to file-based HSQLDB
        findLongEvent(eventDurationMap, eventList);

    }


    /**
     * Method for conversion of string data to Java Object using Jackson-API
     *
     * @param objectMapper
     * @param data
     * @return
     */
    private static Event convertToObject(ObjectMapper objectMapper, String data) {
        try {
            return objectMapper.readValue(data, Event.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to find events which are longer in duration
     *
     * @param eventDurationMap
     * @param eventList
     * @return
     */
    private static void findLongEvent(Map<String, Long> eventDurationMap, List<Event> eventList) {

        logger.info("Finding Longer events and storing them into DB...");
        final long THRESHOLD_VALUE = 4;
        DBUtils.connectToDB();
        for (String key : eventDurationMap.keySet()) {

            String eventData = "";
            String alert;
            logger.info("Event Id : " + key);
            logger.info("Event Duration : " + eventDurationMap.get(key));
            logger.info("Event Type : " + eventList.stream().filter(e -> e.getId().equals(key)).findFirst().get().getType());
            logger.info("Event Host : " + eventList.stream().filter(e -> e.getId().equals(key)).findFirst().get().getHost());
            if (eventDurationMap.get(key) > THRESHOLD_VALUE) {
                logger.info("Event Alert : " + "True");
                alert = "True";
            } else {
                logger.info("Event Alert : " + "False");
                alert = "False";
            }

            eventData = key + "," + eventDurationMap.get(key) + ","
                    + eventList.stream().filter(e -> e.getId().equals(key)).findFirst().get().getType()
                    + "," + eventList.stream().filter(e -> e.getId().equals(key)).findFirst().get().getHost()
                    + "," + alert;
            logger.debug("eventData : " + eventData);


            //Below logic is to insert data into DB using Gradle custom task [insertDataIntoDB]
            logger.info("***************** Insert Data into HSQL DB using gradle task : [insertDataIntoDB] **************");
            String writeData = System.getProperty("user.dir")+"\\gradlew.bat insertDataIntoDB -DeventData=" + eventData + "";
            runGradleTask(writeData);


        }

    }


    /**
     * method to init log4j configurations
     */
    private static void init() {
        DOMConfigurator.configure(System.getProperty("user.dir")+"\\src\\main\\resources\\log4j.xml");
    }

    /**
     * method to run gradle task from java class
     * @param command
     */
    private static void runGradleTask(String command) {
        logger.info("Gradle task started..." + command);
        try {
            Runtime.getRuntime().exec(command);
            logger.info("Gradle task completed..." + command);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }

}


