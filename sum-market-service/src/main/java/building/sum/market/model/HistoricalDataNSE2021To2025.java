package building.sum.market.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historical_data_nse_2021-2025")
public class HistoricalDataNSE2021To2025 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "historical_data_id")
	private Long historicalDataId;

	@Column(name = "symbol", length = 30, nullable = false)
	private String symbol;

	@Column(name = "traded_date", nullable = false)
	private LocalDateTime tradedDate;

	@Column(name = "volume")
	private Long volume;

	@Column(name = "open")
	private BigDecimal open;

	@Column(name = "close")
	private BigDecimal close;

	@Column(name = "high")
	private BigDecimal high;

	@Column(name = "low")
	private BigDecimal low;

	@Column(name = "adjusted_close")
	private BigDecimal adjustedClose;

	@Column(name = "created_date_time", nullable = false)
	private LocalDateTime createdDateTime;

	@Override
	public String toString() {
		return historicalDataId + "," + symbol + "," + tradedDate + "," + volume + "," + open + "," + close + "," + high
				+ "," + low + "," + adjustedClose + "," + createdDateTime;
	}

}
