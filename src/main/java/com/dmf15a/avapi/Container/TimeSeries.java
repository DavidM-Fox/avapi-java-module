package com.dmf15a.avapi.Container;

import com.dmf15a.avapi.ApiQuery;
import com.dmf15a.avapi.Printer.TablePrinter;
import com.dmf15a.avapi.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class TimeSeries extends ApiQuery {


    public enum Type {INTRADAY, DAILY, WEEKLY, MONTHLY, COUNT}

    public class MetaInfo {
        public String symbol;
        public String market;
        public TimeSeries.Type type;
        public String interval;
        public Boolean adjusted;
        public String title;
        public ArrayList<String> headers;
        public String outputSize;

        public MetaInfo() {
            this.symbol = "";
            this.market = "";
            this.type = Type.COUNT;
            this.interval = "";
            this.adjusted = false;
            this.title = "";
            this.headers = new ArrayList<>();
            this.outputSize = "compact";
        }

        public MetaInfo(String symbol, String market, Type type, Boolean adjusted, String interval, String title, String outputSize) {
            this.symbol = symbol;
            this.market = market;
            this.type = type;
            this.adjusted = adjusted;
            this.interval = interval;
            this.title = title;
            this.headers = new ArrayList<>();
            this.outputSize = outputSize;
        }

        public MetaInfo(MetaInfo info) {
            this.symbol = info.symbol;
            this.market = info.market;
            this.type = info.type;
            this.adjusted = info.adjusted;
            this.interval = info.interval;
            this.title = info.title;
            this.headers = new ArrayList<>(info.headers);
            this.outputSize = info.outputSize;
        }
    }

    public MetaInfo info;
    public ArrayList<TimePair> data;

    public TimeSeries() {
        super("");
        this.info = new MetaInfo();
        this.data = new ArrayList<>();
    }

    public TimeSeries(String symbol, String apiKey, String market, Type type, Boolean adjusted, String interval, String outputSize) throws IOException {
        super(apiKey);
        this.info = new MetaInfo(symbol, market, type, adjusted, interval, "", outputSize);
        update();
    }

    public TimeSeries(TimeSeries series) {
        super(series.getApiKey());
        this.info = new MetaInfo(series.info);
        this.data = new ArrayList<>(series.data);
    }

    public void update() throws IOException {

        Boolean warning = false;
        if (info.type == Type.COUNT) {
            System.err.println("WARNING: No type set, cannot update\n" +
                    "\tat com.dmf15a.avapi.Container.TimeSeries\n");
            warning = true;
        }
        if (info.symbol.equals("")) {
            System.err.println("WARNING: No symbol set, cannot update\n" +
                    "\tat com.dmf15a.avapi.Container.TimeSeries\n");
            warning = true;
        }
        if (warning)
            return;

        String function = functionStrings.get(info.type);
        StringBuilder sbTitle = new StringBuilder(info.symbol + ": " + function);

        // INTRADAY Check (different fields)
        if (info.type == TimeSeries.Type.INTRADAY) {
            addQuery("function", function);

            if (info.interval.equals(""))
                info.interval = defaultInterval;

            addQuery("interval", info.interval);
            addQuery("adjusted", info.adjusted.toString());
            sbTitle.append(" (").append(info.interval).append(", adjusted=").append(info.adjusted.toString()).append(")");
        } else {
            if (info.adjusted) {
                addQuery("function", functionStrings.get(info.type) + "_ADJUSTED");
                sbTitle.append(" (adjusted=true");
            } else {
                addQuery("function", functionStrings.get(info.type));
                sbTitle.append(" (adjusted=false)");
            }
        }

        // Add additional query fields
        addQuery("symbol", info.symbol);
        addQuery("outputsize", info.outputSize);
        addQuery("datatype", "csv");

        // Create new TimeSeries and update from it
        TimeSeries series = new TimeSeries(Utils.parseCsvContent(getResponse()));
        this.info.headers = series.info.headers;
        this.info.title = sbTitle.toString();
        this.data = series.data;
    }

    public int colCount() {
        return data.get(0).data.size() + 1;
    }

    public int rowCount() {
        return data.size();
    }

    public void print() {
        // Default parameter (0 = print all)
        print(0);
    }

    public void print(int count) {
        // Create printer object and print header block
        TablePrinter printer = new TablePrinter(info.title, info.headers, 14);
        printer.printHeader();

        // Print up to count (0 = print all)
        int n = count;
        if (count > rowCount() || count == 0)
            n = rowCount();

        // Print TimeSeries' data
        for (int i = 0; i < n; ++i)
            printer.printDataRow(printer.centerData(data.get(i).timestamp, data.get(i).data));
    }

    private static final String defaultInterval = "30min";
    private static final Map<Type, String> functionStrings = new EnumMap<>(TimeSeries.Type.class) {{
        put(TimeSeries.Type.INTRADAY, "TIME_SERIES_INTRADAY");
        put(TimeSeries.Type.DAILY, "TIME_SERIES_DAILY");
        put(TimeSeries.Type.WEEKLY, "TIME_SERIES_WEEKLY");
        put(TimeSeries.Type.MONTHLY, "TIME_SERIES_MONTHLY");
    }};

}
