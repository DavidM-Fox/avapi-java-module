package com.dmf15a.avapi.Company.Stock;
import com.dmf15a.avapi.ApiQuery;
import com.dmf15a.avapi.Container.TimeSeries;
import com.dmf15a.avapi.Utils;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class Stock extends ApiQuery {

    public Stock() {
        super("");
        this.symbol = "";
        this.outputSize = "compact";
    }

    public Stock(String symbol, String apiKey) {
        super(apiKey);
        this.symbol = symbol;
        this.outputSize = "compact";
    }

    public TimeSeries getTimeSeries(TimeSeries.Type type, Boolean adjusted) throws IOException {
        return getCsvData(type, adjusted, "");
    }

    public TimeSeries getTimeSeries(TimeSeries.Type type, Boolean adjusted, String interval) throws IOException {
        return getCsvData(type, adjusted, interval);
    }

    private TimeSeries getCsvData(TimeSeries.Type type, Boolean adjusted, String interval) throws IOException {

        String function = functionStrings.get(type);
        StringBuilder sbTitle = new StringBuilder(symbol + ": " + function);

        // INTRADAY has different query fields...
        if (type == TimeSeries.Type.INTRADAY) {
            addQuery("function", function);

            if (interval.equals(""))
                interval = defaultInterval;

            addQuery("interval", interval);
            addQuery("adjusted", adjusted.toString());
            sbTitle.append(" (").append(interval).append(", adjusted=").append(adjusted.toString()).append(")");
        }
        // Not INTRADAY
        else {
            if (adjusted) {
                addQuery("function", functionStrings.get(type) + "_ADJUSTED");
                sbTitle.append(" (adjusted=true");
            }
            else {
                addQuery("function", functionStrings.get(type));
                sbTitle.append(" (adjusted=false)");
            }
        }

        addQuery("symbol", symbol);
        addQuery("outputsize", outputSize);
        addQuery("datatype", "csv");


        TimeSeries series = new TimeSeries(Utils.parseCsvResponse(getResponse()));
        series.info.symbol = symbol;
        series.info.type = type;
        series.info.adjusted = adjusted;
        series.info.title = sbTitle.toString();
        return series;
    }


    public String symbol;
    public String outputSize;

    private static final String defaultInterval = "30min";
    private static final Map<TimeSeries.Type, String> functionStrings = new EnumMap<>(TimeSeries.Type.class) {{
        put(TimeSeries.Type.INTRADAY, "TIME_SERIES_INTRADAY");
        put(TimeSeries.Type.DAILY, "TIME_SERIES_DAILY");
        put(TimeSeries.Type.WEEKLY, "TIME_SERIES_WEEKLY");
        put(TimeSeries.Type.MONTHLY, "TIME_SERIES_MONTHLY");
    }};
}
