package ma.xproce.stocksmicroservice.dao.repositories;


import ma.xproce.stocksmicroservice.dao.entities.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByTicker(String ticker);
    Page<Stock> findByNameContainingOrTickerContaining(String name, String ticker, Pageable pageable);
    Stock findByName(String name);
}