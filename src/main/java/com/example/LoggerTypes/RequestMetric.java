package com.example.LoggerTypes;
public class RequestMetric {
    private String url;
    private ResponseTimes response_times;
    private StatusCodes status_codes;

    public RequestMetric(String url, ResponseTimes responseTimes, StatusCodes statusCodes) {
        this.url = url;
        this.response_times = responseTimes;
        this.status_codes = statusCodes;
    }

    // Inner class for response times
    public static class ResponseTimes {
        private int min;
        private double percentile_50;
        private double percentile_90;
        private double percentile_95;
        private double percentile_99;
        private int max;

        public ResponseTimes(int min, double p50, double p90, double p95, double p99, int max) {
            this.min = min;
            this.percentile_50 = p50;
            this.percentile_90 = p90;
            this.percentile_95 = p95;
            this.percentile_99 = p99;
            this.max = max;
        }
    }

    // Inner class for status codes
    public static class StatusCodes {
        private int XX1;
        private int XX2;
        private int XX3;
        private int XX4;
        private int XX5;

        public StatusCodes(int xx1, int xx2, int xx3, int xx4, int xx5) {
            this.XX1 = xx1;
            this.XX2 = xx2;
            this.XX3 = xx3;
            this.XX4 = xx4;
            this.XX5 = xx5;
        }
    }
}