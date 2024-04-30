package building.sum.notification.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.notification.model.EmailSentInfo;

public interface EmailSentInfoRepository extends JpaRepository<EmailSentInfo, Long> {

}
