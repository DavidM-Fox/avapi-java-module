package com.dmf15a.avapi.Container;

import com.dmf15a.avapi.Printer.TablePrinter;

import java.util.ArrayList;

public class TimeSeries {

    public enum Type {INTRADAY, DAILY, WEEKLY, MONTHLY, COUNT}

    public static class MetaInfo {
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
        this.info = new MetaInfo();
        this.data = new ArrayList<>();
    }

    public TimeSeries(TimeSeries series) {
        this.info = new MetaInfo(series.info);
        this.data = new ArrayList<>(series.data);
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
}
