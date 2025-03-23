package building.sum.inventory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import building.sum.inventory.model.TableLastUpdateDetails;

public interface TableLastUpdateDetailsRepository extends MongoRepository<TableLastUpdateDetails, String> {

}
