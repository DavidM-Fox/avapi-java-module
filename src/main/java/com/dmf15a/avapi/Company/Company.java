package com.dmf15a.avapi.Company;

import java.io.IOException;

public class Company {

    public static class MetaInfo {
        public String symbol;
        public String apiKey;
        public MetaInfo(String ...args) {
            this.symbol = args[0];
            this.apiKey = args[1];
        }
    }

    public Company.MetaInfo info;
    private Stock stock;
    private Earnings earnings;

    public Company(String symbol, String apiKey) {
        this.info = new MetaInfo(symbol, apiKey);
    }

    public Stock stock() {
        if (stock == null)
            stock = new Stock(info);
        return stock;
    }
    public Earnings earnings() throws IOException {
        if (stock == null)
            earnings = new Earnings(info);
        return earnings;
    }
}
