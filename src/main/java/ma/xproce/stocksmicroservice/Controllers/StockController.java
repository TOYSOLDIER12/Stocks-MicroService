package ma.xproce.stocksmicroservice.Controllers;



import ma.xproce.stocksmicroservice.Services.TokenValidator;
import ma.xproce.stocksmicroservice.dao.entities.Stock;

import ma.xproce.stocksmicroservice.Services.StockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private  StockManager stockManager;
    @Autowired
    private  TokenValidator tokenValidationService;

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token.");
    }

    @GetMapping
    public ResponseEntity<?> getAllStocks(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null || !tokenValidationService.validateToken(token)) {
            return unauthorized();
        }
        List<Stock> stocks = stockManager.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/ticker/{ticker}")
    public ResponseEntity<?> getStockByTicker(@PathVariable String ticker, @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);
        if (token == null || !tokenValidationService.validateToken(token)) {
            return unauthorized();
        }
        Stock stock = stockManager.getStockByTicker(ticker);
        return stock != null ? ResponseEntity.ok(stock) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createStock(@RequestBody Stock stock, @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null || !tokenValidationService.validateToken(token)) {
            return unauthorized();
        }
        Stock createdStock = stockManager.addStock(stock);
        return ResponseEntity.ok(createdStock);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getStockByName(@PathVariable String name, @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null || !tokenValidationService.validateToken(token)) {
            return unauthorized();
        }
        Stock stock = stockManager.getStockByName(name);
        return stock != null ? ResponseEntity.ok(stock) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchStocks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String ticker,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);
        if (token == null || !tokenValidationService.validateToken(token)) {
            return unauthorized();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Stock> stocks = stockManager.searchStocks(name, ticker, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("results", stocks.getContent());
        response.put("totalPages", stocks.getTotalPages());
        response.put("totalElements", stocks.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        if (token == null || !tokenValidationService.validateToken(token)) {
            return unauthorized();
        }
        stockManager.deleteStock(id);
        return ResponseEntity.noContent().build();
    }



}