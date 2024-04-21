package building.sum.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.inventory.model.Fund;

public interface FundRepository extends JpaRepository<Fund, Long> {

}
