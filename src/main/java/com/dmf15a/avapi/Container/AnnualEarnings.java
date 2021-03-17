package com.dmf15a.avapi.Container;

import com.dmf15a.avapi.Printer.TablePrinter;

import java.util.ArrayList;
import java.util.Arrays;

public class AnnualEarnings {

    public static class Report {
        public final String fiscalDateEnding;
        public final String reportedEPS;

        public Report(String fiscalDateEnding, String reportedEPS) {
            this.fiscalDateEnding = fiscalDateEnding;
            this.reportedEPS = reportedEPS;
        }
    }

    public final String symbol;
    public ArrayList<Report> earningsData;

    public AnnualEarnings(String symbol) {
        this.symbol = symbol;
        this.earningsData = new ArrayList<>();
    }

    public int rowCount() {
        return earningsData.size();
    }

    public void print(int count) {
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("Fiscal Date Ending", "Reported EPS"));
        TablePrinter printer = new TablePrinter(String.format("%s: Annual Earnings", symbol), headers, 20);
        printer.printHeader();

        // Print up to count (0 = print all)
        int n = count;
        if (count > rowCount() || count == 0)
            n = rowCount();

        for (int i = 0; i < n; ++i) {
            printer.printDataRow(new ArrayList<String>(Arrays.asList(
                    printer.center(earningsData.get(i).fiscalDateEnding),
                    TablePrinter.formatFloatString(earningsData.get(i).reportedEPS)
            )));
        }
    }
}
