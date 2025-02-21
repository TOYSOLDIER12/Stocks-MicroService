package ma.xproce.stocksmicroservice.Services;

import ma.xproce.stocksmicroservice.dao.entities.Stock;
import ma.xproce.stocksmicroservice.dao.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
@Service
public class StockService implements StockManager{
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    FileReaderInt fileReaderInt;

    @Override
    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public Stock updateStock(Stock stock) {
        return stockRepository.save(stock);
    }


    @Override
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public Stock getStock(Long id) {
        return stockRepository.findById(id).orElse(null);
    }

    @Override
    public Stock getStockByTicker(String ticker) {
        return stockRepository.findByTicker(ticker);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
    @Override
    public void populateEnterprise() {
        if (stockRepository.findAll().isEmpty()) {
            List<String> datas = fileReaderInt.readFile("static/stocks.txt");
            for (String data : datas) {
                String[] parts = data.split(": ");
                if (parts.length == 2) { // Make sure split worked properly
                    Stock stock = new Stock();
                    stock.setName(parts[0].trim());   // "Yum! Brands"
                    stock.setTicker(parts[1].trim()); // "YUM"
                    stockRepository.save(stock);
                } else {
                    System.err.println("Invalid line format: " + data);
                }
            }
        }
    }

    @Override
    public Page<Stock> searchStocks(String name, String ticker, Pageable pageable) {
        if ((name == null || name.isBlank()) && (ticker == null || ticker.isBlank())) {
            // Return all stocks if no search params are provided
            return stockRepository.findAll(pageable);
        }
        return stockRepository.findByNameContainingOrTickerContaining(name, ticker, pageable);

    }

    @Override
    public Stock getStockByName(String name) {
        return stockRepository.findByName(name);
    }


}
