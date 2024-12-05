package com.example;

import java.io.File;

import com.example.LoggerTypes.APMLogger;
import com.example.LoggerTypes.ApplicationLogger;
import com.example.LoggerTypes.RequestLogger;

public class App {
    public static void main(String[] args) {
        File file = new File("/home/guna/SchoolProjects/202-Logger-Project/src/main/resources/SampleLog.txt");
        runLoggingProcess(file);
    }

    public static void runLoggingProcess(File file) {
        APMLogger logger = new APMLogger(file);
        ApplicationLogger logger2 = new ApplicationLogger(file);
        RequestLogger logger3 = new RequestLogger(file);

        String request = "Log this request";

        logger.ExtractData();
        logger.FormatJSONData();
        logger.setHandlerNext(logger2);
        logger.handleRequest(request);

        logger2.ExtractData();
        logger2.FormatJSONData();
        logger2.setHandlerNext(logger3);
        logger2.handleRequest(request);

        logger3.ExtractData();
        logger3.FormatJSONData();
        logger3.handleRequest(request);
    }
}
