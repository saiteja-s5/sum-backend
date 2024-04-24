package building.sum.inventory.model;

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
@Table(name = "dropdown_configuration")
public class DropdownConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dropdown_key")
	private Long dropdownKey;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "dropdown_group", length = 50, nullable = false)
	private String dropdownGroup;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "dropdown_display_name", length = 200, nullable = false)
	private String dropdownDisplayName;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "dropdown_value", length = 50, nullable = false)
	private String dropdownValue;

	@Column(name = "dropdown_order")
	private Integer dropdown_order;

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
		return dropdownKey + "," + dropdownGroup + "," + dropdownDisplayName + "," + dropdownValue + ","
				+ dropdown_order;
	}

}
