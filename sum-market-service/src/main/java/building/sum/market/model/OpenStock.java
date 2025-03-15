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
import jakarta.validation.constraints.Min;
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
@Table(name = "open_stock_holdings")
public class OpenStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "open_stock_id")
	private Long openStockId;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "stock_name", nullable = false, columnDefinition = "NVARCHAR(1500)")
	private String stockName;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "stock_symbol", length = 30, nullable = false)
	private String stockSymbol;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "{mandatory}")
	@Column(name = "bought_market", length = 10, nullable = false)
	private Market boughtMarket;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "investment_date", nullable = false)
	private LocalDateTime investmentDate;

	@NotNull(message = "{mandatory}")
	@Min(value = 1, message = "{numeric-minimum}")
	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@NotNull(message = "{mandatory}")
	@Digits(integer = 10, fraction = 2, message = "{decimal-places}")
	@DecimalMin(value = "0.01", message = "{decimal-minimum-2-digits}")
	@Column(name = "buy_price", nullable = false)
	private BigDecimal buyPrice;

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
		return openStockId + "," + stockName + "," + stockSymbol + "," + boughtMarket.getExtension() + ","
				+ investmentDate + "," + quantity + "," + buyPrice + "," + isActive + "," + createdBy + ","
				+ createdDateTime + "," + updatedBy + "," + updatedDateTime + "," + userJoinKey;
	}

}
