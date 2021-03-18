package com.dmf15a.avapi.Company;

import com.dmf15a.avapi.Container.GlobalQuote;
import com.dmf15a.avapi.Container.TimeSeries;

import java.io.IOException;

public class Stock {

    public Stock.MetaInfo info;

    public static class MetaInfo {
        public String symbol;
        public String apiKey;
        public String outputSize;

        public MetaInfo(String... args) {
            this.symbol = args[0];
            this.apiKey = args[1];
            this.outputSize = args[2];
        }
    }

    public Stock() {
        this.info = new MetaInfo("", "", "compact");
    }

    public Stock(String symbol, String apiKey) {
        this.info = new MetaInfo(symbol, apiKey, "compact");
    }

    public Stock(Company.MetaInfo info) {
        this.info = new MetaInfo(info.symbol, info.apiKey, "compact");
    }

    public GlobalQuote getGlobalQuote() throws IOException {
        return new GlobalQuote(info.symbol, info.apiKey);
    }

    public TimeSeries getTimeSeries(TimeSeries.Type type, Boolean adjusted, String interval) throws IOException {
        return new TimeSeries(info.symbol, info.apiKey, "USD", type, adjusted, interval, info.outputSize);
    }

    // @Overload: interval = "" (Not TimeSeries.Type.INTRADAY)
    public TimeSeries getTimeSeries(TimeSeries.Type type, Boolean adjusted) throws IOException {
        return getTimeSeries(type, adjusted, "");
    }
}
