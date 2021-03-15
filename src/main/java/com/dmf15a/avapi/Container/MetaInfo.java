package com.dmf15a.avapi.Container;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

public class MetaInfo {

    public String symbol;
    public String market;
    public String title;
    public ArrayList<String> headers;
    public TimeSeries.Type type;
    public boolean adjusted;

    public MetaInfo() {
        this.symbol = "";
        this.market = "";
        this.title = "";
        this.headers = new ArrayList<>();
        this.type = TimeSeries.Type.COUNT;
        this.adjusted = false;
    }

    public MetaInfo(MetaInfo info) {
        this.symbol = info.symbol;
        this.market = info.market;
        this.title = info.title;
        this.headers = info.headers;
        this.type = info.type;
        this.adjusted = info.adjusted;
    }

}
