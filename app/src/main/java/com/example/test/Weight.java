package com.example.test;

public class Weight
{
    private String metric;

    private String imperial;

    public String getMetric ()
    {
        return metric;
    }

    public void setMetric (String metric)
    {
        this.metric = metric;
    }

    public String getImperial ()
    {
        return imperial;
    }

    public void setImperial (String imperial)
    {
        this.imperial = imperial;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [metric = "+metric+", imperial = "+imperial+"]";
    }
}
