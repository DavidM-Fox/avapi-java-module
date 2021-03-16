package com.dmf15a.avapi.Company.Stock;

import com.dmf15a.avapi.ApiQuery;
import com.dmf15a.avapi.Printer.TablePrinter;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.dmf15a.avapi.Utils.toUnixTimestamp;

public class GlobalQuote extends ApiQuery {

    public class MetaInfo {
        public String symbol;
        public String market;
        public String title;
        public ArrayList<String> headers;
        public MetaInfo() {
            this.symbol = "";
            this.market = "";
            this.title = "";
            this.headers = new ArrayList<>();
        }
    }

    public MetaInfo info;
    public long timestamp;
    public ArrayList<Float> data;

    public GlobalQuote() {
        super("");
        this.info = new MetaInfo();
        this.timestamp = 0;
        this.data = new ArrayList<>();
    }

    public GlobalQuote(String symbol, String apiKey) throws IOException {
        super(apiKey);
        this.info = new MetaInfo();
        this.info.symbol = symbol;
        this.info.market = "USD";
        this.info.title = new StringBuilder().append(symbol).append(": GLOBAL_QUOTE (USD)").toString();
        this.data = new ArrayList<>();
        update();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void update() throws IOException {

        if (getApiKey() == "") {
            System.err.println(new StringBuilder("WARNING: No API Key set\n")
                    .append("\tat com.dmf15a.avapi.Company.Stock.GlobalQuote")
                    .toString());
            return;
        }

        // Only three parameters needed for GlobalQuote
        addQuery("function", "GLOBAL_QUOTE");
        addQuery("symbol", info.symbol);
        addQuery("datatype", "csv");

        // Query Alpha Vantage API and get response
        StringWriter csv = new StringWriter();
        IOUtils.copy((InputStream) getResponse(), csv, "UTF-8");

        // Parse csv data
        try (BufferedReader reader = new BufferedReader(new StringReader(csv.toString()))) {

            // Get headers
            String line = reader.readLine();
            info.headers = new ArrayList<>(Arrays.asList(line.split(",")));

            // Get data
            line = reader.readLine();
            ArrayList<String> data = new ArrayList<>(Arrays.asList(line.split(",")));

            // Swap symbol/timestamp columns
            Collections.swap(info.headers, 0, 6);
            Collections.swap(data, 0, 6);

            // Remove symbol column, convert data into Floats
            info.headers.remove(6);
            data.remove(6);

            // Convert/remove timestamp data column
            timestamp = toUnixTimestamp(data.get(0));
            data.remove(0);

            String percent = data.get(7).replaceAll("%", "");
            data.set(7, percent);

            for (String value : data) {
                this.data.add(Float.parseFloat(value));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        // Create printer object and print header block
        TablePrinter printer = new TablePrinter(info.title, info.headers, 14);
        printer.printHeader();

        // Print GlobalQuote data
        printer.printDataRow(printer.centerData(this.timestamp, this.data));
    }
}
