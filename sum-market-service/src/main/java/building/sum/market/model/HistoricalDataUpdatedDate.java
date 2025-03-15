package building.sum.market.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "historical_data_updated_date")
public class HistoricalDataUpdatedDate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "historical_data_updated_date_id")
	private Long historicalDataUpdatedDateId;

	@Column(name = "stock_name", nullable = false, columnDefinition = "NVARCHAR(1500)")
	private String stockName;

	@Column(name = "stock_symbol", length = 30, nullable = false)
	private String stockSymbol;

	@Enumerated(EnumType.STRING)
	@Column(name = "market", length = 10, nullable = false)
	private Market market;

	@Column(name = "stock_updated_date", nullable = false)
	private LocalDateTime stockUpdatedDateTime;

	@Column(name = "created_by", length = 50, nullable = false)
	private String createdBy;

	@Column(name = "created_date_time", nullable = false)
	private LocalDateTime createdDateTime;

	@Column(name = "updated_by", length = 50, nullable = false)
	private String updatedBy;

	@Column(name = "updated_date_time", nullable = false)
	private LocalDateTime updatedDateTime;

	@Override
	public String toString() {
		return historicalDataUpdatedDateId + "," + stockName + "," + stockSymbol + "," + market.getExtension() + ","
				+ stockUpdatedDateTime + "," + createdBy + "," + createdDateTime + "," + updatedBy + ","
				+ updatedDateTime;
	}

}
