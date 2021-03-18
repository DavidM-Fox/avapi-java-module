package com.dmf15a.avapi.Company;

import com.dmf15a.avapi.ApiQuery;
import com.dmf15a.avapi.Container.AnnualEarnings;
import com.dmf15a.avapi.Container.QuarterlyEarnings;
import com.dmf15a.avapi.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

public class Earnings extends ApiQuery {

    public class MetaInfo {
        public String symbol;
        public String market;
        public MetaInfo(String ...args) {
            this.symbol = args[0];
            this.market = args[1];
        }
    }

    public AnnualEarnings annual;
    public QuarterlyEarnings quarterly;
    public Earnings.MetaInfo info;


    public Earnings(String symbol, String apiKey) throws IOException {
        super(apiKey);
        this.info = new MetaInfo(symbol, "USD");
        this.annual = new AnnualEarnings(symbol);
        this.quarterly = new QuarterlyEarnings(symbol);
        update();
    }

    public Earnings(Company.MetaInfo info) throws IOException {
        super(info.apiKey);
        this.info = new MetaInfo(info.symbol, "USD");
        this.annual = new AnnualEarnings(info.symbol);
        this.quarterly = new QuarterlyEarnings(info.symbol);
        update();
    }

    public void update() throws IOException {

        if (getApiKey().equals("")) {
            System.err.println("WARNING: No API Key set\n" +
                    "\tat com.dmf15a.avapi.Company.Earnings");
            return;
        }

        // Only two parameters needed for earnings
        addQuery("function", "EARNINGS");
        addQuery("symbol", info.symbol);

        JsonObject jsonData = Utils.parseJsonContent(getResponse());

        // Parse annual earnings
        JsonArray annual = jsonData.get("annualEarnings").getAsJsonArray();
        for (JsonElement entry : annual) {
            JsonObject data = entry.getAsJsonObject();
            AnnualEarnings.Report report = new AnnualEarnings.Report(
                    data.get("fiscalDateEnding").getAsString(),
                    data.get("reportedEPS").getAsString()
            );
            this.annual.earningsData.add(report);
        }

        // Parse quarterly earnings
        JsonArray quarterly = jsonData.get("quarterlyEarnings").getAsJsonArray();
        for (JsonElement entry : quarterly) {
            JsonObject data = entry.getAsJsonObject();
            QuarterlyEarnings.Report report = new QuarterlyEarnings.Report(
                    data.get("fiscalDateEnding").getAsString(),
                    data.get("reportedDate").getAsString(),
                    data.get("reportedEPS").getAsString(),
                    data.get("estimatedEPS").getAsString(),
                    data.get("surprise").getAsString(),
                    data.get("surprisePercentage").getAsString()
            );
            this.quarterly.earningsData.add(report);
        }
    }
}
