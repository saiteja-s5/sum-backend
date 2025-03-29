package building.sum.report.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import building.sum.report.model.ReportTemplate;

public interface ReportTemplateRepository extends MongoRepository<ReportTemplate, String> {

}
