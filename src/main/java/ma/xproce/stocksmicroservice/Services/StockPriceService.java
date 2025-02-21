package ma.xproce.stocksmicroservice.Services;

import ma.xproce.stocksmicroservice.dao.entities.CompanyPrice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockPriceService implements StockPriceManager {

    private final RestTemplate restTemplate;

    @Value("${finnhub.api.key}")
    private String finnhubApiKey;

    public StockPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("stockPrices")
    public Map<String, Object> getLatestPrice(String stockSymbol) {
        Map<String, Object> response = new HashMap<>();

        // Build the Finnhub API URL
        String url = UriComponentsBuilder.fromHttpUrl("https://finnhub.io/api/v1/quote")
                .queryParam("symbol", stockSymbol)            // Stock symbol (e.g., "AAPL")
                .queryParam("token", finnhubApiKey)           // Your Finnhub API key
                .toUriString();

        // Send the GET request
        Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);

        // Extract data and map it to CompanyPrice
        if (apiResponse != null && apiResponse.containsKey("c")) {
            float currentPrice = Float.parseFloat(apiResponse.get("c").toString());   // Current price
            float previousClose = apiResponse.containsKey("pc") ?
                    Float.parseFloat(apiResponse.get("pc").toString()) : -1f;         // Previous close
            float open = apiResponse.containsKey("o") ?
                    Float.parseFloat(apiResponse.get("o").toString()) : -1f;          // Open price
            float high = apiResponse.containsKey("h") ?
                    Float.parseFloat(apiResponse.get("h").toString()) : -1f;          // High price
            float low = apiResponse.containsKey("l") ?
                    Float.parseFloat(apiResponse.get("l").toString()) : -1f;          // Low price

            // Fill the response
            response.put("success", true);
            response.put("results", Arrays.asList(
                    new CompanyPrice(
                            stockSymbol,
                            System.currentTimeMillis(),
                            currentPrice,  // Last price
                            previousClose, // Previous close price
                            open,          // Open price
                            high,          // High price
                            low,           // Low price
                            -1f,           // Mid price (not available in Finnhub API)
                            -1f,           // Volume (not available in this endpoint)
                            -1f,           // Bid size (not available)
                            -1f,           // Bid price (not available)
                            -1f,           // Ask size (not available)
                            -1f            // Ask price (not available)
                    )
            ));
        } else {
            response.put("success", false);
            response.put("results", Collections.emptyList());
        }

        // Log any errors if the response doesn't contain expected data
        if (apiResponse != null && apiResponse.containsKey("error")) {
            System.err.println("Error from Finnhub: " + apiResponse.get("error"));
        }

        return response;
    }
}
