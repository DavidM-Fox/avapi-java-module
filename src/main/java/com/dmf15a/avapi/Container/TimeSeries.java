package com.dmf15a.avapi.Container;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

public class TimeSeries {

    public enum Type {INTRADAY, DAILY, WEEKLY, MONTHLY, COUNT}

    public MetaInfo info;
    public ArrayList<TimePair> data;
    public int cellWidth;

    public TimeSeries() {
        this.info = new MetaInfo();
        this.data = new ArrayList<>();
        this.cellWidth = 14;
    }

    public TimeSeries(TimeSeries series) {
        this.info = new MetaInfo(series.info);
        this.data = new ArrayList<>(series.data);
        this.cellWidth = 14;
    }

    public int colCount() {
        return data.get(0).data.size() + 1;
    }

    public int rowCount() {
        return data.size();
    }

    public void print() {
        // Default parameter
        print(0);
    }

    public void print(int count) {
        // Build separator string
        int nCols = colCount();
        int tableWidth = (nCols * cellWidth) + nCols + 1;
        String separator = getSeparator(tableWidth);

        // Build format string
        StringBuilder sbFormat = new StringBuilder("|");
        for (var i = 0; i < nCols; ++i)
            sbFormat.append("%").append(cellWidth).append("s|");
        String format = sbFormat.append("\n").toString();

        // Print Title/Headers Box
        System.out.println(separator);
        System.out.format(String.format("|%%%ds|\n", tableWidth - 2), center(info.title, tableWidth - 2));
        System.out.println(separator);
        System.out.format(format, center(info.headers).toArray());
        System.out.println(separator);

        // We will print up to count...
        int n = count;
        int nRows = rowCount();
        if (count > nRows)
            n = nRows;
        else if (count == 0)
            n = nRows;

        // Print TimeSeries' data
        for (int i = 0; i < n; ++i)
            System.out.format(format, formatData(data.get(i).timestamp, data.get(i).data).toArray());
    }

    private String getSeparator(int tableWidth) {
        // Create a line separator based on tableWidth
        StringBuilder separator = new StringBuilder();
        separator.append("-".repeat(Math.max(0, tableWidth)));
        return separator.toString();
    }

    private ArrayList<String> formatData(long timestamp, ArrayList<Float> data) {
        // Create new ArrayList and add centered timestamp/data
        ArrayList<String> centered = new ArrayList<>();
        centered.add(center(timestamp));
        data.forEach((value) -> centered.add(String.format("%.2f", value)));
        return centered;
    }

    private String center(long timestamp) {
        return StringUtils.center(String.valueOf(timestamp), cellWidth);
    }

    private String center(String str, int centerWidth) {
        return StringUtils.center(str, centerWidth);
    }

    private ArrayList<String> center(ArrayList<String> headers) {
        // Create new ArrayList and add centered headers
        ArrayList<String> centered = new ArrayList<>();
        headers.forEach((header) -> centered.add(StringUtils.center(header, cellWidth)));
        return centered;
    }
}
