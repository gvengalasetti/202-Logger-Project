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

public class RequestLogger extends GeneralLogger {
    private GeneralLogger nextHandler;
    Map<String, List<Integer>> groupedData = new HashMap<>();
    Map<String, List<Integer>> groupedData2 = new HashMap<>();

    //Map<String, List<Integer>> groupedData = new HashMap<>();

    public RequestLogger(File Inputfile) {
        super(Inputfile);
    }

    @Override
    public void handleRequest(String request){
        System.out.println("RequestLogger handling request: " + request);
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
                String[] parts = line.split(" ");
                String request_url = null;
                String response_time_ms = "";
                String response_status = "";

                 for (String part : parts) {
                    if (part.startsWith("request_url=")) {
                        request_url = part.split("=")[1].replace("\"","").trim();
                    }
                    if (part.startsWith("response_status=")) {
                        response_status = part.split("=")[1];
                    }
                     if (part.startsWith("response_time_ms=")) {
                        response_time_ms = part.split("=")[1];
                        helperExtractData(request_url, response_time_ms, response_status);

                    }       
            }
            }
        } catch (IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

    public void helperExtractData(String request_url, String response_time_ms, String response_status) {
        String key = request_url;

        Integer val = Integer.parseInt(response_time_ms);
        Integer val2 = Integer.parseInt(response_status);
        
        // Ensure the list is initialized before adding values
        groupedData.putIfAbsent(key, new ArrayList<>());
        groupedData.get(key).add(val);

        // Ensure the list is initialized before adding values
        groupedData2.putIfAbsent(key, new ArrayList<>());
        groupedData2.get(key).add(val2);
    }

    public void FormatJSONData() {
        Map<String, RequestMetric> metricsMap = new HashMap<>();
        
        // Process each URL
        for (Map.Entry<String, List<Integer>> entry : groupedData.entrySet()) {
            String url = entry.getKey();
            List<Integer> responseTimes = entry.getValue();
            List<Integer> statusCodes = groupedData2.getOrDefault(url, new ArrayList<>());
            
            // Create metrics for this URL
            RequestMetric metric = createRequestMetric(url, responseTimes, statusCodes);
            
            // Use the URL directly as the key without escaping
            metricsMap.put(url, metric);
        }

        writeMetricsToJson(metricsMap);
    }

    public RequestMetric createRequestMetric(String url, List<Integer> responseTimes, List<Integer> statusCodes) {
        // Create ResponseTimes object
        RequestMetric.ResponseTimes responseTimesMetrics = new RequestMetric.ResponseTimes(
            Collections.min(responseTimes),
            calculatePercentile(responseTimes, 50),
            calculatePercentile(responseTimes, 90),
            calculatePercentile(responseTimes, 95),
            calculatePercentile(responseTimes, 99),
            Collections.max(responseTimes)
        );

        // Calculate status code counts
        int count1xx = (int) statusCodes.stream().filter(code -> code >= 100 && code < 200).count();
        int count2xx = (int) statusCodes.stream().filter(code -> code >= 200 && code < 300).count();
        int count3xx = (int) statusCodes.stream().filter(code -> code >= 300 && code < 400).count();
        int count4xx = (int) statusCodes.stream().filter(code -> code >= 400 && code < 500).count();
        int count5xx = (int) statusCodes.stream().filter(code -> code >= 500 && code < 600).count();

        RequestMetric.StatusCodes statusCodeMetrics = new RequestMetric.StatusCodes(
            count1xx, count2xx, count3xx, count4xx, count5xx
        );

        return new RequestMetric(url, responseTimesMetrics, statusCodeMetrics);
    }

    public void writeMetricsToJson(Map<String, RequestMetric> metricsMap) {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        
        try (FileWriter writer = new FileWriter("Request.json")) {
            gson.toJson(metricsMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double calculatePercentile(List<Integer> values, double percentile) {
        Collections.sort(values); // Sort the values
        int index = (int) Math.ceil(percentile / 100.0 * values.size()) - 1; // Calculate the index
        return values.get(index); // Return the value at the calculated index
}

    public static double calculateMedian(List<Integer> list) {
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




