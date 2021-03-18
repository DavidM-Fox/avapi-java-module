package com.dmf15a.avapi.Company;

import com.dmf15a.avapi.ApiQuery;
import com.dmf15a.avapi.Container.AnnualEarnings;
import com.dmf15a.avapi.Container.QuarterlyEarnings;
import com.dmf15a.avapi.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Earnings extends ApiQuery {

    public class MetaInfo {
        public String symbol;
        public String market;

        public MetaInfo() {
            this.symbol = "";
            this.market = "";
        }
    }

    public AnnualEarnings annual;
    public QuarterlyEarnings quarterly;
    public Earnings.MetaInfo info;


    public Earnings(String symbol, String apiKey) throws IOException {
        super(apiKey);
        this.info = new MetaInfo();
        this.info.symbol = symbol;
        this.info.market = "USD";
        this.annual = new AnnualEarnings(symbol);
        this.quarterly = new QuarterlyEarnings(symbol);
        update();
    }

    public void update() throws IOException {

        if (getApiKey() == "") {
            System.err.println(new StringBuilder("WARNING: No API Key set\n")
                    .append("\tat com.dmf15a.avapi.Company.Earnings")
                    .toString());
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
