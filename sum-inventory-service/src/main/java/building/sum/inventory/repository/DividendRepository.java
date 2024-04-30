package building.sum.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.inventory.model.Dividend;

public interface DividendRepository extends JpaRepository<Dividend, Long> {

	Optional<Dividend> findByUserJoinKeyAndDividendId(String userJoinKey, Long dividendId);

	List<Dividend> findAllByUserJoinKey(String userJoinKey);

	void deleteByUserJoinKeyAndDividendId(String userJoinKey, Long dividendId);

}
