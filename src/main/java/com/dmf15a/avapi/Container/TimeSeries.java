package com.dmf15a.avapi.Container;

import java.util.ArrayList;

public class TimeSeries {

    public enum Type {INTRADAY, DAILY, WEEKLY, MONTHLY, COUNT}

    public MetaInfo info;
    public ArrayList<TimePair> data;

    public TimeSeries() {
        this.info = new MetaInfo();
        this.data = new ArrayList<>();
    }

    public TimeSeries(TimeSeries series) {
        this.info = new MetaInfo(series.info);
        this.data = new ArrayList<>(series.data);
    }
}
