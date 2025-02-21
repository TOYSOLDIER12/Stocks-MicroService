package ma.xproce.stocksmicroservice.Services;

import java.util.Map;

public interface StockPriceManager {
    public Map<String, Object> getLatestPrice(String stockSymbol);
}
