package com.dmf15a.avapi.Container;

import com.dmf15a.avapi.Printer.TablePrinter;

import java.util.ArrayList;
import java.util.Arrays;

public class QuarterlyEarnings {

    public static class Report {
        public final String fiscalDateEnding;
        public final String reportedDate;
        public final String reportedEPS;
        public final String estimatedEPS;
        public final String surprise;
        public final String surprisePercentage;

        public ArrayList<String> reportData;

        public Report(String fiscalDateEnding,
                      String reportedDate,
                      String reportedEPS,
                      String estimatedEPS,
                      String surprise,
                      String surprisePercentage) {

            this.fiscalDateEnding = fiscalDateEnding;
            this.reportedDate = reportedDate;
            this.reportedEPS = reportedEPS;
            this.estimatedEPS = estimatedEPS;
            this.surprise = surprise;
            this.surprisePercentage = surprisePercentage;

            this.reportData = new ArrayList<>(Arrays.asList(
                    this.fiscalDateEnding,
                    this.reportedDate,
                    this.reportedEPS,
                    this.estimatedEPS,
                    this.surprise,
                    this.surprisePercentage
            ));
        }
    }

    public final String symbol;
    public ArrayList<QuarterlyEarnings.Report> earningsData;

    public QuarterlyEarnings(String symbol) {
        this.symbol = symbol;
        this.earningsData = new ArrayList<>();
    }

    public int rowCount() {
        return earningsData.size();
    }

    public void print(int count) {

        // Format title/headers
        String title = new StringBuilder(symbol).append(": Quarterly Earnings").toString();
        ArrayList<String> headers = new ArrayList<>(Arrays.asList(
                "Fiscal Date Ending",
                "Reported Date",
                "Reported EPS",
                "Estimated EPS",
                "Surprise",
                "Surprise %"
        ));

        // New printer object, print headers box
        TablePrinter printer = new TablePrinter(title, headers, 20);
        printer.printHeader();

        // Print up to count (0 = print all)
        int n = count;
        if (count > rowCount() || count == 0)
            n = rowCount();

        for (int i = 0; i < n; ++i) {
            ArrayList<String> printData = new ArrayList<>(earningsData.get(i).reportData);
            String estimatedEPS = earningsData.get(i).estimatedEPS;

            if (estimatedEPS.equals("None")) {
                // Format/Parse (Don't parse indices 3-5)
                printData.set(0, printer.center(printData.get(0)));
                printData.set(1, printer.center(printData.get(1)));
                printData.set(2, TablePrinter.formatFloatString(printData.get(2)));
                printer.printDataRow(printData);
            } else {
                // Format/Parse normally
                printData.set(0, printer.center(printData.get(0)));
                printData.set(1, printer.center(printData.get(1)));
                printData.set(2, TablePrinter.formatFloatString(printData.get(2)));
                printData.set(3, TablePrinter.formatFloatString(printData.get(3)));
                printData.set(4, TablePrinter.formatFloatString(printData.get(4)));
                printData.set(5, TablePrinter.formatFloatString(printData.get(5)));
                printer.printDataRow(printData);
            }
        }
    }
}
