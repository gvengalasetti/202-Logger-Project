package com.example.LoggerTypes;
public class APMmetric {
    private String name;
    private double minimum;
    private double median;
    private double average;
    private double max;

    public APMmetric(String name, double minimum, double median, double average, double max) {
        this.name = name;
        this.minimum = minimum;
        this.median = median;
        this.average = average;
        this.max = max;
    }

    // Getters and setters (if needed)
}