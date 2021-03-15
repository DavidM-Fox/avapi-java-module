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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static TimeSeries parseCsvResponse(Object response) throws IOException {

        StringWriter csv = new StringWriter();
        IOUtils.copy((InputStream)response, csv, "UTF-8");

        // Parse csv data into TimeSeries
        TimeSeries series = new TimeSeries();
        try (BufferedReader reader = new BufferedReader(new StringReader(csv.toString()))) {

            // The

            // Get CSV Headers
            String line = reader.readLine();
            ArrayList<String> headers = new ArrayList<>(Arrays.asList(line.split(",")));

            // Read each line from CSV string
            ArrayList<TimePair> timePairList = new ArrayList<TimePair>();
            line = reader.readLine();
            while(line != null) {

                // Split line by commas
                ArrayList<String> lineData = new ArrayList<>(Arrays.asList(line.split(",")));

                // Convert values to Floats (Skip timestamp column)
                ArrayList<Float> data = new ArrayList<Float>();
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

    public static JsonObject parseJsonResponse(Object response)
    {
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) response));
        return root.getAsJsonObject();
    }

    public static long toUnixTimestamp(String time)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS", Locale.ENGLISH); //Specify your locale

        long unixTime = 0;
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30")); //Specify your timezone
        try {
            unixTime = dateFormat.parse(time).getTime();
            unixTime = unixTime / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unixTime;
    }
}
