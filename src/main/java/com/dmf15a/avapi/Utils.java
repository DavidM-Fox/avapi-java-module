package com.dmf15a.avapi;

import com.dmf15a.avapi.Container.TimePair;
import com.dmf15a.avapi.Container.TimeSeries;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

    public static TimeSeries parseCsvContent(Object content) throws IOException {

        StringWriter csv = new StringWriter();
        IOUtils.copy((InputStream) content, csv, "UTF-8");

        // Parse CSV data into TimeSeries
        TimeSeries series = new TimeSeries();
        try (BufferedReader reader = new BufferedReader(new StringReader(csv.toString()))) {

            // Get CSV Headers
            String line = reader.readLine();
            ArrayList<String> headers = new ArrayList<>(Arrays.asList(line.split(",")));

            // Read each line from CSV string
            ArrayList<TimePair> timePairList = new ArrayList<>();
            line = reader.readLine();
            while (line != null) {

                // Split line by commas
                ArrayList<String> lineData = new ArrayList<>(Arrays.asList(line.split(",")));

                // Convert values to Floats (skip timestamp column)
                ArrayList<Float> data = new ArrayList<>();
                for (int i = 1; i < lineData.size(); ++i) {
                    data.add(Float.parseFloat(lineData.get(i)));
                }

                // Add new TimePair to the list (Convert timestamp column)
                timePairList.add(new TimePair(toUnixTimestamp(lineData.get(0)), data));
                line = reader.readLine();
            }

            // Set TimeSeries data and headers
            series.data = timePairList;
            series.info.headers = headers;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return series;
    }

    public static JsonObject parseJsonContent(Object content) {
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) content));
        return root.getAsJsonObject();
    }

    public static Long toUnixTimestamp(String time) {
        List<String> formatStrings = Arrays.asList("yyyy-MM-dd HH:MM:SS", "yyyy-MM-dd");
        for (String format : formatStrings) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-5:00"));
                return dateFormat.parse(time).getTime() / 1000;
            } catch (ParseException e) { }
        }
        return null;
    }

    public static String readApiKey(String filePath) throws IOException {
        BufferedReader brTest = new BufferedReader(new FileReader(filePath));
        return brTest.readLine();
    }
}
