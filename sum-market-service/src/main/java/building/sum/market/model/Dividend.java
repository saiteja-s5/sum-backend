package building.sum.market.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
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
@Table(name = "dividend")
public class Dividend {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dividend_id")
	private Long dividendId;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "company_name", nullable = false, columnDefinition = "NVARCHAR(1500)")
	private String companyName;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "company_symbol", length = 30, nullable = false)
	private String companySymbol;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "credited_date", nullable = false)
	private LocalDateTime creditedDate;

	@NotNull(message = "{mandatory}")
	@Digits(integer = 10, fraction = 2, message = "{decimal-places}")
	@DecimalMin(value = "0.01", message = "{decimal-minimum-2-digits}")
	@Column(name = "credited_amount", nullable = false)
	private BigDecimal creditedAmount;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "{mandatory}")
	@Column(name = "market", length = 10, nullable = false)
	private Market market;

	@NotNull(message = "{mandatory}")
	@Column(name = "is_active", nullable = false)
	private Integer isActive;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "created_by", length = 50, nullable = false)
	private String createdBy;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "created_date_time", nullable = false)
	private LocalDateTime createdDateTime;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "updated_by", length = 50, nullable = false)
	private String updatedBy;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "updated_date_time", nullable = false)
	private LocalDateTime updatedDateTime;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "user_join_key", nullable = false, columnDefinition = "NVARCHAR(20)")
	private String userJoinKey;

	@Override
	public String toString() {
		return dividendId + "," + companyName + "," + companySymbol + "," + creditedDate + "," + creditedAmount + ","
				+ market + "," + isActive + "," + createdBy + "," + createdDateTime + "," + updatedBy + ","
				+ updatedDateTime + "," + userJoinKey;
	}

}
