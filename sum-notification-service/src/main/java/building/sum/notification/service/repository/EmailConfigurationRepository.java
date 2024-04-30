package building.sum.notification.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.notification.model.EmailConfiguration;

public interface EmailConfigurationRepository extends JpaRepository<EmailConfiguration, Long> {

	Optional<EmailConfiguration> findByEmailCodeIgnoreCase(String emailCode);

}
