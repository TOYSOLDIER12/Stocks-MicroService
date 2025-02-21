package ma.xproce.stocksmicroservice.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
@Service
public class StockDataService implements StockDataManager {

    @Value("${prediction.service.url}")
    private String oldPricesServiceUrl;

    @Autowired
    private RestTemplate restTemplate;



    @Override
    public String fetchStockData(String symbol) throws IOException, InterruptedException {
        try {
            // Call FastAPI service to fetch the stock data as JSON
            String url = oldPricesServiceUrl + "/json?symbol=" + symbol;  // Assuming FastAPI will return JSON
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();  // Return the JSON content
            } else {
                throw new RuntimeException("Failed to fetch stock data: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching stock data", e);
        }
    }

}
