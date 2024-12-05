package com.example.LoggerTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class APMLogger extends GeneralLogger {
    private GeneralLogger nextHandler;

    Map<String, List<Integer>> groupedData = new HashMap<>();

    public APMLogger(File Inputfile) {
        super(Inputfile);
    }

    @Override
    public void handleRequest(String request){
        System.out.println("APMLogger handling request: " + request);
        // Pass the request to the next handler if it exists
        if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }

    @Override
    public void ExtractData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Inputfile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into key-value pairs
                String[] parts = line.split(" ");
                String metric = null;
                String value = null;

                // Iterate over the parts to find metric and value
                for (String part : parts) {
                    if (part.startsWith("metric=")) {
                        metric = part.split("=")[1];
                    }
                    if (part.startsWith("value=")) {
                        value = part.split("=")[1];
                        //System.out.println("metric " + metric + ", value: " + (value));
                        helperExtractData(metric, value);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

    public void helperExtractData(String metric, String value) {
        String key = metric;
        Integer val = Integer.parseInt(value);
        if (!groupedData.containsKey(key)) {
            groupedData.put(key, new ArrayList<>());
        }
        groupedData.get(key).add(val);
    }

    public void FormatJSONData() {
        List<APMmetric> metricsList = new ArrayList<>(); // Initialize a list to hold APMmetrics
        // Iterate over each entry in groupedData
        for (Map.Entry<String, List<Integer>> entry : groupedData.entrySet()) {
            String key = entry.getKey(); // This should be the metric name (e.g., "cpu_usage_percent")
            List<Integer> values = entry.getValue();
            // Calculate statistics and create APMmetric object
            APMmetric apmMetric = createAPMmetric(key, values);
            metricsList.add(apmMetric); // Add the APMmetric to the list
        }
        // Convert the metrics list to JSON and write to file
        writeMetricsToJson(metricsList);
    }

    public APMmetric createAPMmetric(String key, List<Integer> values) {
        // Calculate statistics
        int min = Collections.min(values);
        int max = Collections.max(values);
        double average = values.stream().mapToInt(Integer::intValue).average().orElse(0);
        double median = calculateMedian(values);
        // Create and return the APMmetric object
        return new APMmetric(key, min, median, average, max);
    }

    private void writeMetricsToJson(List<APMmetric> metricsList) {
        // Convert the metrics list directly to JSON
        String jsonOutput = convertToJson(metricsList); // Convert the list to JSON
        // Write JSON output to APM.json
        try (FileWriter fileWriter = new FileWriter("APM.json")) {
            fileWriter.write(jsonOutput);
            System.out.println("JSON data has been written to APM.json");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public static double calculateMedian(List<Integer> values) {
        // Convert to a mutable list
        List<Integer> mutableValues = new ArrayList<>(values);
        Collections.sort(mutableValues);
        int size = mutableValues.size();
        if (size % 2 == 0) {
            return (mutableValues.get(size / 2 - 1) + mutableValues.get(size / 2)) / 2.0;
        } else {
            return mutableValues.get(size / 2);
        }
    }

    public String convertToJson(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Enable pretty printing
        return gson.toJson(object);
}
}
