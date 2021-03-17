package com.dmf15a.avapi.Printer;


import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;


public class TablePrinter {

    private final int nCols;
    private final int tableW;
    private final int cellW;
    private final String title;
    private final ArrayList<String> headers;
    private final String separator;
    private final String format;

    public TablePrinter(String title, ArrayList<String> headers, int cellW) {
        this.nCols = headers.size();
        this.tableW = (nCols * cellW) + nCols + 1;
        this.cellW = cellW;
        this.title = title;
        this.headers = headers;

        // Create a line separator based on tableWidth
        StringBuilder sbSep = new StringBuilder();
        sbSep.append("-".repeat(Math.max(0, tableW)));
        this.separator = sbSep.toString();

        // Build format string
        StringBuilder sbFormat = new StringBuilder("|");
        for (var i = 0; i < nCols; ++i)
            sbFormat.append("%").append(cellW).append("s|");
        this.format = sbFormat.append("\n").toString();
    }

    // Print Title/Headers Box
    public void printHeader() {
        System.out.println(separator);
        System.out.format(String.format("|%%%ds|\n", tableW - 2), center(title, tableW - 2));
        System.out.println(separator);
        System.out.format(format, center(headers).toArray());
        System.out.println(separator);
    }

    // Print a single data row
    public void printDataRow(ArrayList<String> data) {
        System.out.format(format, data.toArray());
    }


    // Parse string as float, set precision, return as String
    public static String formatFloatString(String str) {
        str.replaceAll("%", "");
        String value = String.format("%.2f", Float.parseFloat(str));
        return value;
    }

    // Return a formatted ArrayList<String> of timestamp and float data
    public ArrayList<String> centerData(long timestamp, ArrayList<Float> data) {
        ArrayList<String> centered = new ArrayList<>();
        centered.add(center(timestamp));
        data.forEach((value) -> centered.add(String.format("%.2f", value)));
        return centered;
    }

    // Return a centered timestamp String based on cellW
    public String center(long timestamp) {
        return StringUtils.center(String.valueOf(timestamp), cellW);
    }

    // @Overload Return a centered String based on centerWidth
    public static String center(String str, int centerW) {
        return StringUtils.center(str, centerW);
    }

    // @Overload Return a centered String based on centerWidth
    public String center(String str) {
        return StringUtils.center(str, this.cellW);
    }

    // @Overload Return an ArrayList<String> of centered Strings based on cellW
    public ArrayList<String> center(ArrayList<String> headers) {
        ArrayList<String> centered = new ArrayList<>();
        headers.forEach((header) -> centered.add(StringUtils.center(header, cellW)));
        return centered;
    }
}
