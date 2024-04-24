package building.sum.notification.model;

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
@Table(name = "email_sent_info")
public class EmailSentInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "email_sent_info_key")
	private Long emailSentInfoKey;

	@NotNull(message = "{mandatory}")
	@Column(name = "email_key", nullable = false)
	private Integer emailKey;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "email_sent_date_time", nullable = false)
	private LocalDateTime emailSentDateTime;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "email_sent_to", length = 200, nullable = false)
	private String emailSentTo;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "email_sent_from", length = 50, nullable = false)
	private String emailSentFrom;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "email_sent_status", length = 50, nullable = false)
	private String emailSentStatus;

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
		return emailSentInfoKey + "," + emailKey + "," + emailSentDateTime + "," + emailSentTo + "," + emailSentFrom
				+ "," + emailSentStatus;
	}

}
