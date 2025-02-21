package ma.xproce.stocksmicroservice.Services;

import java.io.IOException;

public interface StockDataManager {

    public String fetchStockData(String symbol) throws IOException, InterruptedException ;
}
