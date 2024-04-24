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
@Table(name = "email_configuration")
public class EmailConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "email_key")
	private Long emailKey;

	@Column(name = "email_description", length = 200)
	private String emailDescription;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "email_code", length = 50, nullable = false)
	private String emailCode;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "email_from", length = 50, nullable = false)
	private String emailFrom;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "email_to", length = 200, nullable = false)
	private String emailTo;

	@Column(name = "email_cc", length = 200)
	private String emailCc;

	@Column(name = "email_bcc", length = 200)
	private String emailBcc;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "email_subject", length = 200, nullable = false)
	private String emailSubject;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "email_body_template_name", length = 50, nullable = false)
	private String emailBodyTemplateName;

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
		return emailKey + "," + emailDescription + "," + emailCode + "," + emailFrom + "," + emailTo + "," + emailCc
				+ "," + emailBcc + "," + emailSubject + "," + emailBodyTemplateName;
	}

}
