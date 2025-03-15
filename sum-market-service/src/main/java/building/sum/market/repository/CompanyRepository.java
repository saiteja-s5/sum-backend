package building.sum.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	List<Company> findByIsActive(Integer isActive);

}
