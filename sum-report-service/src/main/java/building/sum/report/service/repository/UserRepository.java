package building.sum.report.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.report.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserJoinKeyIgnoreCase(String userJoinKey);

}
