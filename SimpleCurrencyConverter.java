import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.Scanner;

public class SimpleCurrencyConverter {

    // Replace YOUR_API_KEY with your actual API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/3884d48c06849c37bdc872e4/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get base currency from user
        System.out.print("Enter the base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        // Get target currency from user
        System.out.print("Enter the target currency (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        // Get amount from user
        System.out.print("Enter the amount to convert: ");
        double amount = scanner.nextDouble();

        try {
            // Fetch exchange rate
            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

            // Convert amount
            double convertedAmount = amount * exchangeRate;

            // Display result
            System.out.printf("Converted Amount: %.2f %s%n", convertedAmount, targetCurrency);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        String urlStr = API_URL + baseCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONObject("conversion_rates").getDouble(targetCurrency);

        } else {
            throw new Exception("Error fetching exchange rate");
        }
    }
}
