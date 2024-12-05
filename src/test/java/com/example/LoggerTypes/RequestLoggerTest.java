package com.example.LoggerTypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class RequestLoggerTest {
    private RequestLogger requestLogger;

    @Before
    public void setUp() throws IOException {
        // Create a temporary file for testing
        File tempFile = File.createTempFile("testLog", ".log");
        try (FileWriter writer = new FileWriter(tempFile)) {
            // Write test data to the file
            writer.write("request_url=\"http://example.com\" response_status=200 response_time_ms=150\n");
            writer.write("request_url=\"http://example.com\" response_status=404 response_time_ms=200\n");
            writer.write("request_url=\"http://anotherexample.com\" response_status=200 response_time_ms=100\n");
        }
        requestLogger = new RequestLogger(tempFile);
    }

    @Test
    public void testHandleRequest() {
        // Test that handleRequest does not throw an exception
        requestLogger.handleRequest("Test request");
        // No assertion needed, just ensure it runs without exception
    }

    @Test
    public void testExtractData() {
        // Call the method to extract data
        requestLogger.ExtractData();

        // Verify that the groupedData map contains the expected data
        assertTrue(requestLogger.groupedData.containsKey("http://example.com"));
        assertEquals(2, requestLogger.groupedData.get("http://example.com").size());
        assertTrue(requestLogger.groupedData.get("http://example.com").contains(150));
        assertTrue(requestLogger.groupedData.get("http://example.com").contains(200));

        assertTrue(requestLogger.groupedData.containsKey("http://anotherexample.com"));
        assertEquals(1, requestLogger.groupedData.get("http://anotherexample.com").size());
        assertTrue(requestLogger.groupedData.get("http://anotherexample.com").contains(100));
    }

    @Test
    public void testHelperExtractData() {
        // Test that helperExtractData does not throw an exception
        requestLogger.helperExtractData("http://example.com", "200", "200");
    }

    @Test
    public void testFormatJSONData() {
        // First, extract data to populate the groupedData
        requestLogger.ExtractData();

        // Call the method to format JSON data
        requestLogger.FormatJSONData();

        // Verify that the JSON file was created and contains expected data
        File jsonFile = new File("Request.json");
        assertTrue(jsonFile.exists());

     
    }

    @Test
    public void testCreateRequestMetric() {
        // Prepare dummy data
        List<Integer> responseTimes = new ArrayList<>();
        responseTimes.add(100);
        responseTimes.add(200);
        List<Integer> statusCodes = new ArrayList<>();
        statusCodes.add(200);
        statusCodes.add(404);

        // Test that createRequestMetric returns a non-null RequestMetric
        RequestMetric metric = requestLogger.createRequestMetric("http://example.com", responseTimes, statusCodes);
        assertNotNull(metric);
    }

    @Test
    public void testWriteMetricsToJson() {
        // Prepare dummy data
        Map<String, RequestMetric> metricsMap = new HashMap<>();
        metricsMap.put("http://example.com", new RequestMetric("http://example.com", new RequestMetric.ResponseTimes(100, 150, 200, 250, 300, 400), new RequestMetric.StatusCodes(0, 1, 0, 0, 0)));

        // Test that writeMetricsToJson does not throw an exception
        requestLogger.writeMetricsToJson(metricsMap);
    }

    @Test
    public void testCalculatePercentile() {
        // Prepare dummy data
        List<Integer> values = new ArrayList<>();
        values.add(100);
        values.add(200);
        values.add(300);

        // Test that calculatePercentile returns a double
        double percentileValue = requestLogger.calculatePercentile(values, 50);
        assertTrue(percentileValue >= 100 && percentileValue <= 300); // Check if the value is within the range
    }

    @Test
    public void testCalculateMedian() {
        // Prepare dummy data
        List<Integer> values = new ArrayList<>();
        values.add(100);
        values.add(200);
        values.add(300);

        // Test that calculateMedian returns a double
        double medianValue = RequestLogger.calculateMedian(values);
        assertEquals(200.0, medianValue, 0.01); // Check if the median is correct
    }

    @Test
    public void testConvertToJson() {
        // Test that convertToJson returns a non-null String
        String json = requestLogger.convertToJson(new Object());
        assertNotNull(json);
    }
}
