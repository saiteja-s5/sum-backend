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
@Table(name = "user_master")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_key")
	private Long userKey;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "first_name", length = 100, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 100)
	private String lastName;

	@Column(name = "display_name", length = 100)
	private String displayName;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "email_id", length = 150, nullable = false)
	private String emailId;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "contact_number", length = 50, nullable = false)
	private String contactNumber;

	@Column(name = "aadhar_number", length = 50)
	private String aadharNumber;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "pancard_number", length = 50, nullable = false)
	private String pancardNumber;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "password", length = 100, nullable = false)
	private String password;

	@PastOrPresent(message = "{future}")
	@Column(name = "dob")
	private LocalDateTime dob;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "sign_in_date", nullable = false)
	private LocalDateTime signInDate;

	@Column(name = "address", length = 500)
	private String address;

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

	@NotEmpty(message = "{mandatory}")
	@Column(name = "user_join_key", length = 10, nullable = false)
	private String userJoinKey;

	@Override
	public String toString() {
		return userKey + "," + firstName + "," + lastName + "," + displayName + "," + emailId + "," + contactNumber
				+ "," + aadharNumber + "," + pancardNumber + "," + password + "," + dob + "," + signInDate + ","
				+ address + "," + isActive + "," + createdDateTime + "," + createdBy + "," + userJoinKey;
	}

}
