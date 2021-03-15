import com.dmf15a.avapi.ApiQuery;
import com.dmf15a.avapi.Company.Stock.Stock;
import com.dmf15a.avapi.Container.TimeSeries;
import com.google.gson.JsonObject;

import java.io.IOException;

public class MainApp {
    public static void main(String[] args) throws IOException {

        String symbol = "GME";
        String function = "GLOBAL_QUOTE";

        Stock gme = new Stock(symbol, "KEY");
        TimeSeries series = gme.getTimeSeries(TimeSeries.Type.INTRADAY, true, "30min");
    }
}
