package building.sum.market.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import building.sum.market.model.TableLastUpdateDetails;

public interface TableLastUpdateDetailsRepository extends MongoRepository<TableLastUpdateDetails, String> {

}
