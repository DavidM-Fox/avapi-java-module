import com.dmf15a.avapi.Company.Stock.Stock;
import com.dmf15a.avapi.Container.TimeSeries;
import com.dmf15a.avapi.Utils;

import java.io.IOException;

public class MainApp {
    public static void main(String[] args) throws IOException {

        String symbol = "GME";
        String apiKey = Utils.readApiKey("api.key");
        Stock gme = new Stock(symbol, apiKey);

        TimeSeries series = gme.getTimeSeries(TimeSeries.Type.INTRADAY, true);
        series.print(3);
    }
}
