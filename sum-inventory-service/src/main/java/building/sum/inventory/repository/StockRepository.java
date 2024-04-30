package building.sum.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.inventory.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

	Optional<Stock> findByUserJoinKeyAndStockId(String userJoinKey, Long stockId);

	List<Stock> findAllByUserJoinKey(String userJoinKey);

	void deleteByUserJoinKeyAndStockId(String userJoinKey, Long stockId);

}
