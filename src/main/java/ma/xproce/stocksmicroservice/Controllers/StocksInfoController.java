package ma.xproce.stocksmicroservice.Controllers;

import ma.xproce.stocksmicroservice.Services.*;
import ma.xproce.stocksmicroservice.dao.entities.Stock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stocks-info")
public class StocksInfoController {

    @Autowired
    private StockManager stockManager;

    @Autowired
    private StockNewsManager stockNewsManager;

    @Autowired
    private StockPriceManager stockPriceManager;
    @Autowired
    private StockDataService stockDataService;
    @Autowired
    private TokenValidator tokenValidationService;

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token.");
    }


    @GetMapping("/detail/{ticker}")
    public ResponseEntity<?> getStockDetails(@PathVariable String ticker,@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null || !tokenValidationService.validateToken(token)) {
            return unauthorized();
        }
        Stock stock = stockManager.getStockByTicker(ticker);
        if (stock == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "results", List.of()));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("results", List.of(stock));
        return ResponseEntity.ok(response);
    }
    @GetMapping("/news/{ticker}")
    public ResponseEntity<?> getStockNews(@PathVariable String ticker,@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null || !tokenValidationService.validateToken(token)) {
            return unauthorized();
        }
        List<Map<String, Object>> news = stockNewsManager.getLatestNews(ticker);
        if (news == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "results", List.of()));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("results", news);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/price/{ticker}")
    public ResponseEntity<?> getStockPrice(@PathVariable String ticker,@RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);
        if (token == null || !tokenValidationService.validateToken(token)) {
            return unauthorized();
        }
        try {
            Map<String, Object> response = stockPriceManager.getLatestPrice(ticker);
            if ((boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            // Handle any unexpected errors gracefully
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("results", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
        @GetMapping(value = "/chart/historical/{symbol}", produces = "application/json")
        public ResponseEntity<?> getStockData(@PathVariable String symbol,@RequestHeader("Authorization") String authHeader) {

            String token = extractToken(authHeader);
            if (token == null || !tokenValidationService.validateToken(token)) {
                return unauthorized();
            }
            try {
                // Fetch stock data from FastAPI as JSON
                String jsonData = stockDataService.fetchStockData(symbol);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + symbol + ".json");
                return new ResponseEntity<>(jsonData, headers, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to fetch stock data for " + symbol, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


}
