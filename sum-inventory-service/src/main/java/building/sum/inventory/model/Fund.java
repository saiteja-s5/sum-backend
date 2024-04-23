package building.sum.inventory.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "funds")
public class Fund {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fund_id")
	private Long fundId;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "transaction_date", nullable = false)
	private LocalDateTime transactionDate;

	@NotNull(message = "{mandatory}")
	@Digits(integer = 10, fraction = 2, message = "{decimal-places}")
	@DecimalMin(value = "0.01", message = "{decimal-minimum-2-digits}")
	@Column(name = "credited_amount", nullable = false)
	private BigDecimal creditedAmount;

	@NotNull(message = "{mandatory}")
	@Digits(integer = 10, fraction = 2, message = "{decimal-places}")
	@DecimalMin(value = "0.01", message = "{decimal-minimum-2-digits}")
	@Column(name = "debited_amount", nullable = false)
	private BigDecimal debitedAmount;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "created_date_time")
	private LocalDateTime createdDateTime;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "created_by", length = 50)
	private String createdBy;

	@Override
	public String toString() {
		return fundId + "," + transactionDate + "," + creditedAmount + "," + debitedAmount + "," + createdBy + ","
				+ createdDateTime;
	}

}
