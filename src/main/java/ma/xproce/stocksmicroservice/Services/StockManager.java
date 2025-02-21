package ma.xproce.stocksmicroservice.Services;

import ma.xproce.stocksmicroservice.dao.entities.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface StockManager {
    public Stock addStock(Stock stock);
    public Stock updateStock(Stock stock);
    public void deleteStock(Long id);
    public Stock getStock(Long id);
    public Stock getStockByTicker(String ticker);
    public List<Stock> getAllStocks();
    public void populateEnterprise();
    public Page<Stock> searchStocks(String name, String ticker, Pageable pageable);
    public Stock getStockByName(String name);
}
