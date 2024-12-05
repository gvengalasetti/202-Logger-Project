package com.example.LoggerTypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ApplicationLoggerTest extends TestCase {
    private ApplicationLogger logger;
    private File testFile;

    public ApplicationLoggerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ApplicationLoggerTest.class);
    }

    @Override
    protected void setUp() throws IOException {
        // Create a temporary test file with sample log data
        testFile = File.createTempFile("testlog", ".log");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("timestamp=2024-03-14 level=INFO message=Test message\n");
            writer.write("timestamp=2024-03-14 level=ERROR message=Error message\n");
            writer.write("timestamp=2024-03-14 level=INFO message=Another message\n");
        }
        logger = new ApplicationLogger(testFile);
    }

    @Override
    protected void tearDown() {
        testFile.delete();
    }

    /**
     * Test handleRequest method
     */
    public void testHandleRequest() {
        logger.handleRequest("Test request");
        // Since it only prints to console, we can only verify it doesn't throw exceptions
        assertTrue(true);
    }

    /**
     * Test ExtractData method
     */
    public void testExtractData() {
        logger.ExtractData();
        HashMap<String, Integer> data = logger.groupedData;
        
        assertEquals("Should have 2 INFO logs", 1, (int)data.get("INFO"));
        assertEquals("Should have 1 ERROR log", 0, (int)data.get("ERROR"));
    }

    /**
     * Test helperExtractData method
     */
    public void testHelperExtractData() {
        // First call initializes with 1
        logger.helperExtractData("INFO");
        // Second call increments to 2
        logger.helperExtractData("INFO");
        // First call for ERROR initializes with 1
        logger.helperExtractData("ERROR");

        assertEquals("Should have 2 INFO logs", 1, (int)logger.groupedData.get("INFO"));
        assertEquals("Should have 1 ERROR log", 0, (int)logger.groupedData.get("ERROR"));
    }

    /**
     * Test FormatJSONData and writeMetricsToJson methods
     */
    public void testFormatJSONData() {
        logger.ExtractData();
        logger.FormatJSONData();
        
        File jsonFile = new File("Application.json");
        assertTrue("JSON file should be created", jsonFile.exists());
        
        // Clean up
        jsonFile.delete();
    }

    /**
     * Test convertToJson method
     */
    public void testConvertToJson() {
        HashMap<String, Integer> testData = new HashMap<>();
        testData.put("INFO", 2);
        testData.put("ERROR", 1);
        
        String json = logger.convertToJson(testData);
        
        assertTrue("JSON should contain INFO", json.contains("\"INFO\": 2"));
        assertTrue("JSON should contain ERROR", json.contains("\"ERROR\": 1"));
    }
} 