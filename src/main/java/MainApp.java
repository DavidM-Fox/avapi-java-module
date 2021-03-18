import com.dmf15a.avapi.Company.Company;
import com.dmf15a.avapi.Misc;

import java.io.IOException;

public class MainApp {
    public static void main(String[] args) throws IOException {

        String symbol = "GME";
        String apiKey = Misc.readApiKey("api.key");

        Company gme = new Company(symbol, apiKey);
        gme.earnings().quarterly.print(5);
    }
}
