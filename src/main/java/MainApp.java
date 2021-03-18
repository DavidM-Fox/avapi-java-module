import com.dmf15a.avapi.Company.Company;
import com.dmf15a.avapi.Utils;

import java.io.IOException;

public class MainApp {
    public static void main(String[] args) throws IOException {

        String symbol = "GME";
        String apiKey = Utils.readApiKey("api.key");

        Company gme = new Company(symbol, apiKey);
        gme.earnings().annual.print(5);
        gme.earnings().quarterly.print(5);
    }
}
