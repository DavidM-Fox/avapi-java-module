package com.dmf15a.avapi.Company;

import com.dmf15a.avapi.ApiQuery;
import com.dmf15a.avapi.Container.GlobalQuote;
import com.dmf15a.avapi.Container.TimeSeries;
import com.dmf15a.avapi.Misc;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

import static com.dmf15a.avapi.Misc.toUnixTimestamp;

public class Stock extends ApiQuery {

    public static class MetaInfo {
        public String symbol;
        public String outputSize;

        public MetaInfo(String... args) {
            this.symbol = args[0];
            this.outputSize = args[1];
        }
    }

    public Stock.MetaInfo info;

    public Stock() {
        super("");
        this.info = new MetaInfo("", "compact");
    }

    public Stock(String symbol, String apiKey) {
        super(apiKey);
        this.info = new MetaInfo(symbol, "compact");
    }

    public Stock(Company.MetaInfo info) {
        super(info.apiKey);
        this.info = new MetaInfo(info.symbol, "compact");
    }

    public GlobalQuote getGlobalQuote() throws IOException {

        // Check if any required String is empty
        if (Misc.checkIfEmpty(info.symbol, getApiKey())) {
            System.err.println("WARNING: A required ApiQuery field is empty:" +
                    String.format("[symbol=%s]", info.symbol) +
                    String.format("[apiKey=%s]", getApiKey()) + "]: Returning an empty GlobalQuote:\n" +
                    "\tat com.dmf15a.avapi.Company.Stock.getGlobalQuote()");
            return new GlobalQuote();
        }

        // Only three parameters needed for GlobalQuote
        resetQuery();
        addQuery("function", "GLOBAL_QUOTE");
        addQuery("symbol", info.symbol);
        addQuery("datatype", "csv");

        // Query Alpha Vantage API and get response
        StringWriter csv = new StringWriter();
        IOUtils.copy((InputStream) getResponse(), csv, "UTF-8");

        // Parse csv data
        GlobalQuote quote = new GlobalQuote(info.symbol);
        try (BufferedReader reader = new BufferedReader(new StringReader(csv.toString()))) {

            // Get headers
            String line = reader.readLine();
            quote.info.headers = new ArrayList<>(Arrays.asList(line.split(",")));

            // Get data
            line = reader.readLine();
            ArrayList<String> data = new ArrayList<>(Arrays.asList(line.split(",")));

            // Swap symbol/timestamp columns
            Collections.swap(quote.info.headers, 0, 6);
            Collections.swap(data, 0, 6);

            // Remove symbol column
            quote.info.headers.remove(6);
            data.remove(6);

            // Convert/remove timestamp data column
            //noinspection ConstantConditions
            quote.timestamp = toUnixTimestamp(data.get(0));
            data.remove(0);

            // Remove '%' from Percent Change string
            data.set(7, data.get(7).replaceAll("%", ""));

            // Convert each String into Floats
            for (String value : data) {
                quote.data.add(Float.parseFloat(value));
            }
            return quote;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println("EXCEPTION: Returning an empty GlobalQuote.\n" +
                    "\tat com.dmf15a.avapi.Company.Stock.getGlobalQuote()\n");
            return quote;
        }
    }

    public TimeSeries getTimeSeries(TimeSeries.Type type, Boolean adjusted, String interval) throws IOException {

        // Check if any required String is empty
        if (Misc.checkIfEmpty(info.symbol, info.outputSize, getApiKey())) {
            System.err.println("WARNING: A required ApiQuery field is empty:" +
                    String.format("[symbol=%s]", info.symbol) +
                    String.format("[apiKey=%s]", getApiKey()) +
                    String.format("[outputSize=%s]:", info.outputSize) + "Returning an empty TimeSeries:\n" +
                    "\tat com.dmf15a.avapi.Company.Stock.getTimeSeries()\n");
            return new TimeSeries();
        }

        String function = functionStrings.get(type);
        StringBuilder sbTitle = new StringBuilder(info.symbol + ": " + function);

        // INTRADAY Check (different required fields)
        resetQuery();
        if (type == TimeSeries.Type.INTRADAY) {
            if (interval.equals(""))
                interval = defaultInterval;
            addQuery("function", function);
            addQuery("interval", interval);
            addQuery("adjusted", adjusted.toString());
            sbTitle.append(" (").append(interval).append(", adjusted=").append(adjusted.toString()).append(")");
        } else {
            String adj = (adjusted) ? "_ADJUSTED" : "";
            addQuery("function", function + adj);
            sbTitle.append(" (adjusted=").append(adjusted.toString()).append(")");
        }

        // Add additional query fields
        addQuery("symbol", info.symbol);
        addQuery("outputsize", info.outputSize);
        addQuery("datatype", "csv");

        // Create new TimeSeries and update from it
        TimeSeries series = new TimeSeries(Misc.parseCsvContent(getResponse()));
        series.info.title = sbTitle.toString();
        return series;
    }

    // @Overload: interval = "" (Not TimeSeries.Type.INTRADAY)
    public TimeSeries getTimeSeries(TimeSeries.Type type, Boolean adjusted) throws IOException {
        return getTimeSeries(type, adjusted, "");
    }

    private static final String defaultInterval = "30min";
    private static final Map<TimeSeries.Type, String> functionStrings = new EnumMap<>(TimeSeries.Type.class) {{
        put(TimeSeries.Type.INTRADAY, "TIME_SERIES_INTRADAY");
        put(TimeSeries.Type.DAILY, "TIME_SERIES_DAILY");
        put(TimeSeries.Type.WEEKLY, "TIME_SERIES_WEEKLY");
        put(TimeSeries.Type.MONTHLY, "TIME_SERIES_MONTHLY");
    }};
}
