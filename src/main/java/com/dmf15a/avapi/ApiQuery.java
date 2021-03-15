package com.dmf15a.avapi;
import java.io.*;
import java.net.URLConnection;
import java.security.Policy;
import java.util.HashMap;
import java.util.Map;

import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

public class ApiQuery {

    public ApiQuery(String apiKey) {
        this.apiKey = apiKey;
        this.queries = new HashMap<String, String>() {{
            put("apikey", apiKey);
        }};
        //Policy policy = new Policy(java.security.AllPermission);
    }

    private String apiKey;
    private Map<String, String> queries;


    public void setApiKey(String key)
    {
        apiKey = key;
        queries.put("apikey", key);
    }

    public void addQuery(String field, String value)
    {
        if(field.equals("apikey"))
            setApiKey(value);
        else
            queries.put(field, value);
    }

    public Object getResponse() throws IOException {
        URL url = new URL(buildUrl());
        URLConnection request = url.openConnection();
        request.connect();
        return request.getContent();
    }

    private String buildUrl()
    {
        StringBuilder sbUrl = new StringBuilder("https://www.alphavantage.co/query?");
        for (Map.Entry<String, String> entry: queries.entrySet())
        {
            sbUrl.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sbUrl.toString();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1)
        {
            sb.append((char)cp);
        }
        return sb.toString();
    }
}
