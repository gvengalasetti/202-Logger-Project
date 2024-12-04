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

public class ApplicationLogger extends GeneralLogger {
    private GeneralLogger nextHandler;
    Map<String, Integer> groupedData = new HashMap<>();

    public ApplicationLogger(File Inputfile) {
        super(Inputfile);
    }

    @Override
    public void handleRequest(String request){
        System.out.println("Application Logger handling request: " + request);
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
                if (line.contains("metric")) {
                    // System.out.println("yooooza");
                }
                // Split the line into key-value pairs
                String[] parts = line.split(" ");
                String severity = null;

                // Iterate over the parts to find metric and value
                for (String part : parts) {
                    if (part.startsWith("severity=")) {
                        severity = part.split("=")[1];
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

    public void helperExtractData(String severity) {
        String key = severity;
        if (!groupedData.containsKey(key)) {
            groupedData.put(key, 1);
        }
        groupedData.put(key, groupedData.get(key)+1);
    }

    public void FormatJSONData() {
        List<APMmetric> metricsList = new ArrayList<>(); // Initialize a list to hold APMmetrics
        // Print the grouped data for debugging
        System.out.println("Grouped Data: " + groupedData);
        // Iterate over each entry in groupedData
        // for (Map.Entry<String, List<Integer>> entry : groupedData.entrySet()) {
        //     String key = entry.getKey(); // This should be the metric name (e.g., "cpu_usage_percent")
        //     List<Integer> values = entry.getValue();
        //     // Calculate statistics and create APMmetric object
        //     APMmetric apmMetric = createAPMmetric(key, values);
        //     metricsList.add(apmMetric); // Add the APMmetric to the list
        // }
        // // Convert the metrics list to JSON and write to file
        // writeMetricsToJson(metricsList);
    }

    private APMmetric createAPMmetric(String key, List<Integer> values) {
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

    private static double calculateMedian(List<Integer> list) {
        Collections.sort(list);
        int size = list.size();
        if (size % 2 == 0) {
            // Even number of elements: average the two middle elements
            return (list.get(size / 2 - 1) + list.get(size / 2)) / 2.0;
        } else {
            // Odd number of elements: take the middle element
            return list.get(size / 2);
        }
    }

    public String convertToJson(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Enable pretty printing
        return gson.toJson(object);
}

    }
