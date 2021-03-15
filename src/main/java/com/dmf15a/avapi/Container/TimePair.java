package com.dmf15a.avapi.Container;
import java.util.ArrayList;

public class TimePair {

    public TimePair()
    {
        this.timestamp = 0;
        this.data = new ArrayList<Float>();
    }

    public TimePair(long timestamp, ArrayList<Float> data)
    {
        this.timestamp = timestamp;
        this.data = new ArrayList<Float>(data);
    }

    public TimePair(TimePair pair)
    {
        this.timestamp = pair.timestamp;
        this.data = pair.data;
    }

    public long timestamp;
    public ArrayList<Float> data;
}
