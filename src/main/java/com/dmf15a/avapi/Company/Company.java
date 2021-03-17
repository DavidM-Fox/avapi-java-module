package com.dmf15a.avapi.Company;

import com.dmf15a.avapi.Company.Earnings.Earnings;
import com.dmf15a.avapi.Company.Stock.Stock;

import java.io.IOException;

public class Company {

    private String symbol;
    private String apiKey;

    private Stock stock;
    private Earnings earnings;

    public Company(String symbol, String key)
    {
        this.symbol = symbol;
        this.apiKey = key;
    }

    public Stock Stock() {
        if(stock == null)
            stock = new Stock(symbol, apiKey);
        return stock;
    }

    public Earnings Earnings() throws IOException {
        if(stock == null)
            earnings = new Earnings(symbol, apiKey);
        return earnings;
    }
}
