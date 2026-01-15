package com.example.myapp.models;

import java.util.List;

public class PeriodAnalyticsResponse {
    private Period period;
    private List<AnalyticsData> data;

    public Period getPeriod() {
        return period;
    }

    public List<AnalyticsData> getData() {
        return data;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public void setData(List<AnalyticsData> data) {
        this.data = data;
    }

    public static class Period {
        private String start;
        private String end;

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }
}

