package com.dmf15a.avapi;

import java.io.*;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

public class ApiQuery {

    private String apiKey;
    private Map<String, String> queries;

    public ApiQuery(String apiKey) {
        this.apiKey = apiKey;
        this.queries = new HashMap<>() {{
            put("apikey", apiKey);
        }};
    }

    public void setApiKey(String key) {
        apiKey = key;
        queries.put("apikey", key);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void addQuery(String field, String value) {
        if (field.equals("apikey"))
            setApiKey(value);
        else
            queries.put(field, value);
    }

    public void resetQuery() {
        queries.clear();
        queries.put("apikey", apiKey);
    }


    public Object getResponse() throws IOException {
        URL url = new URL(buildUrl());
        URLConnection request = url.openConnection();
        request.connect();
        return request.getContent();
    }

    private String buildUrl() {
        StringBuilder sbUrl = new StringBuilder("https://www.alphavantage.co/query?");
        queries.forEach((key, value) -> sbUrl.append("&").append(key).append("=").append(value));
        return sbUrl.toString();
    }
}
