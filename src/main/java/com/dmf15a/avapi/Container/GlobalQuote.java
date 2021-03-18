package com.dmf15a.avapi.Container;

import com.dmf15a.avapi.Printer.TablePrinter;

import java.util.ArrayList;

public class GlobalQuote {

    public class MetaInfo {
        public String symbol;
        public String market;
        public String title;
        public ArrayList<String> headers;

        public MetaInfo(String... args) {
            this.symbol = args[0];
            this.market = args[1];
            this.title = args[2];
            this.headers = new ArrayList<>();
        }
    }

    public GlobalQuote.MetaInfo info;
    public long timestamp;
    public ArrayList<Float> data;

    public GlobalQuote() {
        this.info = new MetaInfo("", "", "");
        this.timestamp = 0;
        this.data = new ArrayList<>();
    }

    public GlobalQuote(String symbol) {
        this.info = new MetaInfo(symbol, "USD", symbol +
                ": GLOBAL_QUOTE (USD)"
        );
        this.data = new ArrayList<>();
    }

    public void print() {
        // Create printer object and print header block
        TablePrinter printer = new TablePrinter(info.title, info.headers, 14);
        printer.printHeader();

        // Print GlobalQuote data
        printer.printDataRow(printer.centerData(this.timestamp, this.data));
    }
}
