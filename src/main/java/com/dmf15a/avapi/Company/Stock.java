package com.dmf15a.avapi.Company;

import com.dmf15a.avapi.ApiQuery;
import com.dmf15a.avapi.Container.GlobalQuote;
import com.dmf15a.avapi.Container.TimeSeries;
import com.dmf15a.avapi.Utils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

import static com.dmf15a.avapi.Utils.toUnixTimestamp;

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

        if (getApiKey().equals("")) {
            System.err.println("WARNING: No API Key set\n" +
                    "\tat com.dmf15a.avapi.Company.Stock.getGlobalQuote()");
            return new GlobalQuote();
        }

        // Only three parameters needed for GlobalQuote
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

            // Remove symbol column, convert data into Floats
            quote.info.headers.remove(6);
            data.remove(6);

            // Convert/remove timestamp data column
            //noinspection ConstantConditions
            quote.timestamp = toUnixTimestamp(data.get(0));
            data.remove(0);

            // Remove '%' from Percent Change string
            data.set(7, data.get(7).replaceAll("%", ""));

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

        if (info.symbol.equals("")) {
            System.err.println("WARNING: No symbol set, returning an empty TimeSeries.\n" +
                    "\tat com.dmf15a.avapi.Company.Stock.getTimeSeries()\n");
            return new TimeSeries();
        }
        if (info.outputSize.equals("")) {
            System.err.println("WARNING: No output size set, returning an empty TimeSeries.\n" +
                    "\tat com.dmf15a.avapi.Company.Stock.getTimeSeries()\n");
            return new TimeSeries();
        }
        if (getApiKey().equals("")) {
            System.err.println("WARNING: No api key set, returning an empty TimeSeries.\n" +
                    "\tat com.dmf15a.avapi.Company.Stock.getTimeSeries()\n");
            return new TimeSeries();
        }

        String function = functionStrings.get(type);
        StringBuilder sbTitle = new StringBuilder(info.symbol + ": " + function);

        // INTRADAY Check (different fields)
        if (type == TimeSeries.Type.INTRADAY) {
            addQuery("function", function);

            if (interval.equals(""))
                interval = defaultInterval;

            addQuery("interval", interval);
            addQuery("adjusted", adjusted.toString());
            sbTitle.append(" (").append(interval).append(", adjusted=").append(adjusted.toString()).append(")");
        } else {
            if (adjusted) {
                addQuery("function", function + "_ADJUSTED");
                sbTitle.append(" (adjusted=true");
            } else {
                addQuery("function", function);
                sbTitle.append(" (adjusted=false)");
            }
        }

        // Add additional query fields
        addQuery("symbol", info.symbol);
        addQuery("outputsize", info.outputSize);
        addQuery("datatype", "csv");

        // Create new TimeSeries and update from it
        TimeSeries series = new TimeSeries(Utils.parseCsvContent(getResponse()));
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
