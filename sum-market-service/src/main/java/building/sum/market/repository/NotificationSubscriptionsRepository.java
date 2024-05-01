package building.sum.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.NotificationSubscriptions;

public interface NotificationSubscriptionsRepository extends JpaRepository<NotificationSubscriptions, Long> {

	List<NotificationSubscriptions> findBySchedulerType(String schedulerType);

}
