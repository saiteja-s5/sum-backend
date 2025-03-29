package building.sum.report.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import building.sum.report.model.AppDetails;

public interface AppDetailsRepository extends MongoRepository<AppDetails, String> {

}
