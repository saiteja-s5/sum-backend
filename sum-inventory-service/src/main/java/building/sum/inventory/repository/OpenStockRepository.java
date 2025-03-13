package building.sum.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import building.sum.inventory.model.OpenStock;

public interface OpenStockRepository extends JpaRepository<OpenStock, Long> {

	Optional<OpenStock> findByUserJoinKeyAndOpenStockId(String userJoinKey, Long openStockId);

	List<OpenStock> findAllByUserJoinKey(String userJoinKey);

	@Transactional
	void deleteByUserJoinKeyAndOpenStockId(String userJoinKey, Long openStockId);

	@Transactional
	void deleteByUserJoinKey(String userJoinKey);

}
