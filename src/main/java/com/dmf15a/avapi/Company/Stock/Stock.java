package com.dmf15a.avapi.Company.Stock;

import com.dmf15a.avapi.Container.TimeSeries;

import java.io.IOException;

public class Stock {

    public String symbol;
    public String outputSize;
    private String apiKey;

    public Stock() {
        this.symbol = "";
        this.outputSize = "compact";
    }

    public Stock(String symbol, String apiKey) {
        this.symbol = symbol;
        this.apiKey = apiKey;
        this.outputSize = "compact";
    }

    public GlobalQuote getGlobalQuote() throws IOException {
        return new GlobalQuote(symbol, apiKey);
    }

    public TimeSeries getTimeSeries(TimeSeries.Type type, Boolean adjusted, String interval) throws IOException {
        return new TimeSeries(symbol, apiKey, "USD", type, adjusted, interval, outputSize);
    }

    // @Overload: Default parameter "interval"
    public TimeSeries getTimeSeries(TimeSeries.Type type, Boolean adjusted) throws IOException {
        return getTimeSeries(type, adjusted, "");
    }
}
