package com.dmf15a.avapi.Container;
import java.util.ArrayList;

public class TimePair {

    public long timestamp;
    public ArrayList<Float> data;

    public TimePair(long timestamp, ArrayList<Float> data)
    {
        this.timestamp = timestamp;
        this.data = new ArrayList<>(data);
    }
}
