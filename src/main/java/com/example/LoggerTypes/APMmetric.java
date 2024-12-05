package com.example.LoggerTypes;
public class APMmetric {
    public String name;
    public double minimum;
    public double median;
    public double average;
    public double max;
    public String key;

    public APMmetric(String name, double minimum, double median, double average, double max) {
        this.name = name;
        this.minimum = minimum;
        this.median = median;
        this.average = average;
        this.max = max;
    }

    public String getKey() {
        return this.key;
    }

    public int getMinValue() {
        return (int) this.minimum; 
    }

    public int getMaxValue() {
        return (int) this.max; 
    }

    public double getAverage() {
        return this.average; 
    }

    public double getMedian() {
        return this.median; 
    }

}