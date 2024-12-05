package com.example.LoggerTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApplicationLogger extends GeneralLogger {
    private GeneralLogger nextHandler;
    HashMap<String, Integer> groupedData = new HashMap<>();

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
                String[] parts = line.split(" ");
                String level = null;

                // Iterate over the parts to find metric and value
                for (String part : parts) {
                    if (part.startsWith("level=")) {
                        level = part.split("=")[1];
                        helperExtractData(level);
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
        writeMetricsToJson(groupedData);
    }

    private void writeMetricsToJson(HashMap<String, Integer> groupedData) {
        // Convert the metrics list directly to JSON
        String jsonOutput = convertToJson(groupedData); // Convert the list to JSON
        // Write JSON output to APM.json
        try (FileWriter fileWriter = new FileWriter("Application.json")) {
            fileWriter.write(jsonOutput);
            System.out.println("JSON data has been written to Application.json");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }


    public String convertToJson(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Enable pretty printing
        return gson.toJson(object);
}

    }
