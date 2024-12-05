package com.example.LoggerTypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ApplicationLoggerTest {
    private ApplicationLogger logger;
    private File testFile;

    @Before
    public void setUp() throws IOException {
        // Create a temporary file for testing
        testFile = File.createTempFile("testLog", ".txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("level=INFO\n");
            writer.write("level=ERROR\n");
            writer.write("level=INFO\n");
        }
        logger = new ApplicationLogger(testFile);
    }

    @Test
    public void testHandleRequest() {
    
        logger.handleRequest("Test request");
      
    }

    @Test
    public void testExtractData() {
        logger.ExtractData();
        HashMap<String, Integer> expectedData = new HashMap<>();
         expectedData.put("INFO", 2);
        expectedData.put("ERROR", 1);
        assertEquals(expectedData, logger.groupedData);
    }

    @Test
    public void testHelperExtractData() {
        logger.helperExtractData("DEBUG");
        assertEquals(Integer.valueOf(1), logger.groupedData.get("DEBUG"));
        logger.helperExtractData("DEBUG");
        assertEquals(Integer.valueOf(2), logger.groupedData.get("DEBUG"));
    }

    @Test
    public void testFormatJSONData() {
        logger.ExtractData(); // Populate the groupedData first
        logger.FormatJSONData();
        File jsonFile = new File("Application.json");
        assertTrue(jsonFile.exists());
    }

    @Test
    public void testWriteMetricsToJson() {
        logger.ExtractData(); // Populate the groupedData first
        logger.writeMetricsToJson(logger.groupedData);
        File jsonFile = new File("Application.json");
        assertTrue(jsonFile.exists());
    }

    // @Test
    // public void testConvertToJson() {
    //     String jsonOutput = logger.convertToJson(logger.groupedData);
    //     assertNotNull(jsonOutput);
    //     System.out.println(jsonOutput);
    //     assertTrue(jsonOutput.contains("INFO"));
    //     assertTrue(jsonOutput.contains("ERROR"));
    // }
    @Test
    public void testConvertToJson() {
        // Arrange
        RequestLogger requestLogger = new RequestLogger(null); // Passing null for simplicity
        TestObject testObject = new TestObject("John", 30, true);

        // Expected JSON (formatted as pretty-printed JSON)
        String expectedJson = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"isActive\": true\n" +
                "}";

        // Act
        String jsonOutput = requestLogger.convertToJson(testObject);

        // Assert
        assertEquals(expectedJson, jsonOutput);
    }

    // A simple class to test JSON conversion
    static class TestObject {
        private String name;
        private int age;
        private boolean isActive;

        public TestObject(String name, int age, boolean isActive) {
            this.name = name;
            this.age = age;
            this.isActive = isActive;
    }


    @After
    public void tearDown() {
        File jsonFile = new File("Application.json");
        if (jsonFile.exists()) {
            jsonFile.delete();
        }
    }
} 
}