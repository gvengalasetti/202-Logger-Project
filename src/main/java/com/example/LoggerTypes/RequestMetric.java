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
        private int _1XX;
        private int _2XX;
        private int _3XX;
        private int _4XX;
        private int _5XX;

        public StatusCodes(int _1XX, int _2XX, int _3XX, int _4XX, int _5XX) {
            this._1XX = _1XX;
            this._2XX = _2XX;
            this._3XX = _3XX;
            this._4XX = _4XX;
            this._5XX = _5XX;
        }
    }
}