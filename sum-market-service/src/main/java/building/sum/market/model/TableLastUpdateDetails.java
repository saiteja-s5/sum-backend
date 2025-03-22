package building.sum.market.model;

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
@Document(collection = "table_last_update_details")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableLastUpdateDetails {

	@Id
	private String id;

	private LocalDateTime openStockHoldingsUpdatedDateTime;

	private LocalDateTime mutualFundUpdatedDateTime;

	private LocalDateTime fundUpdatedDateTime;

	private LocalDateTime dividendUpdatedDateTime;

	private LocalDateTime closedStockHoldingsUpdatedDateTime;

	private LocalDateTime lastUpdatedDateTime;

	private String lastUpdatedColumn;

	@Override
	public String toString() {
		return id + "," + openStockHoldingsUpdatedDateTime + "," + mutualFundUpdatedDateTime + "," + fundUpdatedDateTime
				+ "," + dividendUpdatedDateTime + "," + closedStockHoldingsUpdatedDateTime + "," + lastUpdatedDateTime
				+ "," + lastUpdatedColumn;
	}

}
