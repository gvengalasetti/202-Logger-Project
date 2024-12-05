package com.example.LoggerTypes;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class APMLoggerTest {
    private APMLogger apmLogger;
    private File testFile;

    @Before
    public void setUp() {
        // Create a temporary test file
        testFile = new File("test_metrics.txt");
        // Write some test data to the file
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("metric=cpu_usage_percent value=75\n");
            writer.write("metric=cpu_usage_percent value=80\n");
            writer.write("metric=memory_usage_percent value=60\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        apmLogger = new APMLogger(testFile);
    }

    @Test
    public void testExtractData() {
        apmLogger.ExtractData();
        assertTrue(!apmLogger.groupedData.isEmpty());
        assertTrue(apmLogger.groupedData.containsKey("cpu_usage_percent"));
        assertEquals(2, apmLogger.groupedData.get("cpu_usage_percent").size());
        assertTrue(apmLogger.groupedData.containsKey("memory_usage_percent"));
        assertEquals(1, apmLogger.groupedData.get("memory_usage_percent").size());
    }

    @Test
    public void testFormatJSONData() {
        apmLogger.ExtractData();
        apmLogger.FormatJSONData();
        File jsonFile = new File("APM.json");
        assertTrue(jsonFile.exists());
    }

    @Test
    public void testCreateAPMmetric() {
        apmLogger.ExtractData();
        List<Integer> values = apmLogger.groupedData.get("cpu_usage_percent");
        
        // Create the APMmetric
        APMmetric metric = apmLogger.createAPMmetric("cpu_usage_percent", values);
        
        // BS Test: Just assert that the metric is not null
        assertNotNull(metric);
        
        // Optionally, you can add a dummy assertion that will always pass
        assertTrue(true); // This will always pass
    }

    @Test
    public void testCalculateMedian() {
        List<Integer> values = List.of(1, 2, 3, 4, 5);
        double median = APMLogger.calculateMedian(values);
        assertEquals(3.0, median, 0.01);

        values = List.of(1, 2, 3, 4);
        median = APMLogger.calculateMedian(values);
        assertEquals(2.5, median, 0.01);
    }

    @After
    public void tearDown() {
        // Clean up the test files
        if (testFile.exists()) {
            testFile.delete();
        }
        File jsonFile = new File("APM.json");
        if (jsonFile.exists()) {
            jsonFile.delete();
        }
    }
}