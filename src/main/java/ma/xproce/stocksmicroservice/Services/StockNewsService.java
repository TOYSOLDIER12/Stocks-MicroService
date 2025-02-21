package ma.xproce.stocksmicroservice.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class StockNewsService implements StockNewsManager {

    private final RestTemplate restTemplate;

    @Value("${newsapi.key}")
    private String newsApiKey;

    // Constructor injection of RestTemplate
    public StockNewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Map<String, Object>> getLatestNews(String stockSymbol) {
        // Build the NewsAPI URL
        String url = UriComponentsBuilder.fromHttpUrl("https://newsapi.org/v2/everything")
                .queryParam("q", stockSymbol)
                .queryParam("sortBy", "publishedAt")  // Sort by published date
                .queryParam("pageSize", 5)            // Limit to 5 results
                .queryParam("apiKey", newsApiKey)     // Your API key from NewsAPI
                .toUriString();

        // Send the GET request
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        // Extract the "articles" from the response body
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("articles")) {
            return (List<Map<String, Object>>) responseBody.get("articles");
        }
        return null;
    }



}
