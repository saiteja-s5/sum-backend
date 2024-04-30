package building.sum.report.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.report.model.AppDetails;

public interface AppDetailsRepository extends JpaRepository<AppDetails, String> {

}
