package building.sum.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.TableLastUpdateDetails;

public interface TableLastUpdateDetailsRepository extends JpaRepository<TableLastUpdateDetails, String> {

}
