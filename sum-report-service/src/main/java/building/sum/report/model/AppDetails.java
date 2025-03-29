package building.sum.report.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "app_details")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppDetails {

	@Id
	private String id;

	private String appName;

	private String appAddress;

	private String appVersion;

	private String appEmail;

	private String appContactNumber;

	private LocalDateTime appCreatedDate;

	private String appAddressCity;

	private String appAddressCountry;

	@Override
	public String toString() {
		return id + "," + appName + "," + appAddress + "," + appVersion + "," + appEmail + "," + appContactNumber + ","
				+ appCreatedDate + "," + appAddressCity + "," + appAddressCountry;
	}

}
