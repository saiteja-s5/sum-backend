package building.sum.notification.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.notification.model.AppDetails;

public interface AppDetailsRepository extends JpaRepository<AppDetails, String> {

	AppDetails findByAppDetailsKey(String appDetailsKey);

}
