package building.sum.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
