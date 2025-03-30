package building.sum.market.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserJoinKeyIgnoreCase(String userJoinKey);

}
