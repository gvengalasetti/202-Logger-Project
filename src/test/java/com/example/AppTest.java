package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class AppTest {
    private File tempLogFile;

    @Before
    public void setUp() throws IOException {
        // Create a temporary log file for testing
        tempLogFile = File.createTempFile("testLog", ".txt");
        try (FileWriter writer = new FileWriter(tempLogFile)) {
            // Write sample log entries to the file
            writer.write("request_url=\"http://example.com\" response_status=200 response_time_ms=150\n");
            writer.write("request_url=\"http://example.com\" response_status=404 response_time_ms=200\n");
            writer.write("request_url=\"http://anotherexample.com\" response_status=200 response_time_ms=100\n");
        }
    }

    @Test
    public void testRunLoggingProcess() {
        // Call the method to run the logging process
        App.runLoggingProcess(tempLogFile);

        // Verify that the JSON file was created
        File jsonFile = new File("Request.json");
        assertTrue(jsonFile.exists());

       
    }
}
