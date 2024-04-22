package building.sum.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.inventory.model.Dividend;

public interface DividendRepository extends JpaRepository<Dividend, Long> {

}
