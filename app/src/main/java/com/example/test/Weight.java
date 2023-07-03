package com.example.test;

public class Weight {
    private String metric;

    private String imperial;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getImperial() {
        return imperial;
    }

    public void setImperial(String imperial) {
        this.imperial = imperial;
    }

    public Double getWeightAvg() {
        String[] range = metric.split("-");
        Double weightAvg = (Double.parseDouble(range[0].trim()) + Double.parseDouble(range[1].trim())) / 2;
        return weightAvg;
    }

    @Override
    public String toString() {
        return "ClassPojo [metric = " + metric + ", imperial = " + imperial + "]";
    }
}
