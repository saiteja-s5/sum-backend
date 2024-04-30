package building.sum.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.inventory.model.TableLastUpdateDetails;

public interface TableLastUpdateDetailsRepository extends JpaRepository<TableLastUpdateDetails, String> {

}
