package building.sum.inventory.model;

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
@Table(name = "stock_holdings")
public class Stock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_id")
	private Long stockId;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "stock_name", length = 200, nullable = false)
	private String stockName;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "stock_symbol", length = 10, nullable = false)
	private String stockSymbol;

	@Enumerated(EnumType.STRING)
	@NotEmpty(message = "{mandatory}")
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
	@PastOrPresent(message = "{future}")
	@Column(name = "created_date_time")
	private LocalDateTime createdDateTime;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "created_by", length = 50)
	private String createdBy;

	@Override
	public String toString() {
		return stockId + "," + stockName + "," + stockSymbol + "," + boughtMarket.getExtension() + "," + investmentDate
				+ "," + quantity + "," + buyPrice + "," + createdBy + "," + createdDateTime;
	}

}
