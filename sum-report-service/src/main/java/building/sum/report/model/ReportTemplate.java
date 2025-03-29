package building.sum.report.model;

import org.bson.types.Binary;
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
@Document(collection = "report_templates")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportTemplate {

	@Id
	private String id;

	private String reportType;

	private Boolean passwordProtected;

	private Integer passwordLength;

	private Binary logo;

	private String logoName;

	private Double headerFontSize;

	private Double bodyFontSize;

	private Double bodyFontSizeSmall;

	private Double paragraphLineSpacing;

	private Double paragraphLineSpacingSmall;

	private String headingText;

	private Double leftPadding;

	private Double topPadding;

}
