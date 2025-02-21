package ma.xproce.stocksmicroservice.Services;

import java.util.List;
import java.util.Map;

public interface StockNewsManager {
    public List<Map<String, Object>> getLatestNews(String stockSymbol);
}
