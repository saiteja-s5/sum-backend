package building.sum.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import building.sum.inventory.model.ClosedStock;

public interface ClosedStockRepository extends JpaRepository<ClosedStock, Long> {

	Optional<ClosedStock> findByUserJoinKeyAndClosedStockId(String userJoinKey, Long closedStockId);

	List<ClosedStock> findAllByUserJoinKey(String userJoinKey);

	@Transactional
	void deleteByUserJoinKeyAndClosedStockId(String userJoinKey, Long closedStockId);

	@Transactional
	void deleteByUserJoinKey(String userJoinKey);

}
