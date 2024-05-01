package building.sum.market.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_subscriptions")
public class NotificationSubscriptions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_subscription_key")
	private Long notificationSubscriptionKey;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "user_join_key", length = 10, nullable = false)
	private String userJoinKey;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "scheduler_type", length = 50, nullable = false)
	private String schedulerType;

	@Column(name = "is_subscribed")
	private Integer isSubscribed;

	@NotNull(message = "{mandatory}")
	@Column(name = "is_active", nullable = false)
	private Integer isActive;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "created_date_time", nullable = false)
	private LocalDateTime createdDateTime;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "created_by", length = 50, nullable = false)
	private String createdBy;

	@Override
	public String toString() {
		return notificationSubscriptionKey + "," + userJoinKey + "," + schedulerType + "," + isSubscribed + ","
				+ isActive + "," + createdDateTime + "," + createdBy;
	}

}
