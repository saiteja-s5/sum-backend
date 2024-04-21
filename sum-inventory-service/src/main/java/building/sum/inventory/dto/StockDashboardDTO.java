package building.sum.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StockDashboardDTO {

	private List<StockDashboardRowDTO> stocks;
	private BigDecimal totalStockInvestmentValue;
	private BigDecimal totalStockCurrentValue;
	private BigDecimal totalStockCurrentReturn;
	private BigDecimal totalStockCurrentReturnPercent;
	private BigDecimal totalStockOnePercentTargetValue;
	private BigDecimal totalStockTwoPercentTargetValue;
	private LocalDateTime stockLastTransactionOn;
	private LocalDateTime stockTableUpdatedOn;

	@Override
	public String toString() {
		return stocks + "," + totalStockInvestmentValue + "," + totalStockCurrentValue + "," + totalStockCurrentReturn
				+ "," + totalStockCurrentReturnPercent + "," + totalStockOnePercentTargetValue + ","
				+ totalStockTwoPercentTargetValue + "," + stockLastTransactionOn + "," + stockTableUpdatedOn;
	}

}
