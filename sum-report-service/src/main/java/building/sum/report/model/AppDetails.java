package building.sum.report.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "app_details")
public class AppDetails {

	@Id
	@Column(name = "app_details_key", length = 50)
	private String appDetailsKey;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "app_name", length = 200, nullable = false)
	private String appName;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "app_address", length = 500, nullable = false)
	private String appAddress;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "app_version", length = 50, nullable = false)
	private String appVersion;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "app_email", length = 100, nullable = false)
	private String appEmail;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "app_contact_number", length = 100, nullable = false)
	private String appContactNumber;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "app_created_date", nullable = false)
	private LocalDateTime appCreatedDate;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "app_address_city", length = 100, nullable = false)
	private String appAddressCity;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "app_address_country", length = 50, nullable = false)
	private String appAddressCountry;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "app_reports_owner_password", length = 50, nullable = false)
	private String appReportsOwnerPassword;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "app_email_password", length = 100, nullable = false)
	private String appEmailPassword;

	@Override
	public String toString() {
		return appDetailsKey + "," + appName + "," + appAddress + "," + appVersion + "," + appEmail + ","
				+ appContactNumber + "," + appCreatedDate + "," + appAddressCity + "," + appAddressCountry + ","
				+ appReportsOwnerPassword + "," + appEmailPassword;
	}

}
