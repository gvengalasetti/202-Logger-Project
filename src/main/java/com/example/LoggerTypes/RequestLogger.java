package com.example.LoggerTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class RequestLogger extends GeneralLogger {
    public HashMap<String, Integer> tracker = new HashMap<String, Integer>();
    private GeneralLogger nextHandler;

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

                if (line.contains("metric")) {
                    // tracker.put(line, null);

                }

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
                        System.out.println("metric " + metric + ", value: " + (value));
                        tracker.put(metric, Integer.parseInt(value));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

}
// class Apmclass{

// }

// Things I dont know:
// try ->
// BufferedReader()->
// fileereader (filepath)
// so basically te .split method for strings
