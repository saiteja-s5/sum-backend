package building.sum.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.inventory.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
