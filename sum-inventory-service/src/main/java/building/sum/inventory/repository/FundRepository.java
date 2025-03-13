package building.sum.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import building.sum.inventory.model.Fund;

public interface FundRepository extends JpaRepository<Fund, Long> {

	Optional<Fund> findByUserJoinKeyAndFundId(String userJoinKey, Long fundId);

	List<Fund> findAllByUserJoinKey(String userJoinKey);

	@Transactional
	void deleteByUserJoinKeyAndFundId(String userJoinKey, Long fundId);

	@Transactional
	void deleteByUserJoinKey(String userJoinKey);

}
