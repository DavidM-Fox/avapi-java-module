package com.dmf15a.avapi.Company;

import com.dmf15a.avapi.Company.Stock.Stock;

public class Company {

    private String symbol;
    private String apiKey;

    private Stock stock;

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
}
