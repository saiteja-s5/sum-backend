package building.sum.market.model;

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
@Table(name = "table_last_update_details")
public class TableLastUpdateDetails {

	@Id
	@Column(name = "table_updated_details_key", length = 50)
	private String tableLastUpdatedId;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "stock_holdings_open_last_updated", nullable = false)
	private LocalDateTime stockHoldingsOpenLastUpdatedOn;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "mutual_fund_last_updated", nullable = false)
	private LocalDateTime mutualFundLastUpdatedOn;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "fund_last_updated", nullable = false)
	private LocalDateTime fundLastUpdatedOn;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "dividend_last_updated", nullable = false)
	private LocalDateTime dividendLastUpdatedOn;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "stock_holdings_closed_last_updated", nullable = false)
	private LocalDateTime stockHoldingsClosedLastUpdatedOn;

	@NotNull(message = "{mandatory}")
	@PastOrPresent(message = "{future}")
	@Column(name = "last_updated_date_time", nullable = false)
	private LocalDateTime lastUpdatedDateTime;

	@NotEmpty(message = "{mandatory}")
	@Column(name = "last_updated_column", length = 50, nullable = false)
	private String lastUpdatedColumn;

	@Override
	public String toString() {
		return tableLastUpdatedId + "," + stockHoldingsOpenLastUpdatedOn + "," + mutualFundLastUpdatedOn + ","
				+ fundLastUpdatedOn + "," + dividendLastUpdatedOn + "," + stockHoldingsClosedLastUpdatedOn + ","
				+ lastUpdatedDateTime + "," + lastUpdatedColumn;
	}

}
