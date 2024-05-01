package building.sum.notification.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

	@Column(name = "email_key")
	private Integer emailKey;

	@PastOrPresent(message = "{future}")
	@Column(name = "email_sent_date_time")
	private LocalDateTime emailSentDateTime;

	@Column(name = "email_sent_to", length = 200)
	private String emailSentTo;

	@Column(name = "email_sent_from", length = 50)
	private String emailSentFrom;

	@Column(name = "email_sent_status")
	private Integer emailSentStatus;

	@Column(name = "is_active")
	private Integer isActive;

	@PastOrPresent(message = "{future}")
	@Column(name = "created_date_time")
	private LocalDateTime createdDateTime;

	@Column(name = "created_by", length = 50)
	private String createdBy;

	@Override
	public String toString() {
		return emailSentInfoKey + "," + emailKey + "," + emailSentDateTime + "," + emailSentTo + "," + emailSentFrom
				+ "," + emailSentStatus;
	}

}
