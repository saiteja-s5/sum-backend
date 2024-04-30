package building.sum.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

	List<Stock> findAllByUserJoinKey(String userJoinKey);

}
