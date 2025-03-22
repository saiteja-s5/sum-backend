package building.sum.market.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClosedStockDashboardDTO {

	private List<ClosedStockDashboardRowDTO> closedStocks;
	private BigDecimal totalProfitLossValue;
	private BigDecimal totalBoughtAmountValue;
	private BigDecimal totalSoldAmountValue;
	private BigDecimal totalProfitLossPercentage;
	private BigDecimal percentReturnPerTransaction;
	private LocalDateTime closedStockLastTransactionOn;
	private LocalDateTime closedStockTableUpdatedOn;

	@Override
	public String toString() {
		return closedStocks + "," + totalProfitLossValue + "," + totalBoughtAmountValue + "," + totalSoldAmountValue
				+ "," + totalProfitLossPercentage + "," + percentReturnPerTransaction + ","
				+ closedStockLastTransactionOn + "," + closedStockTableUpdatedOn;
	}

}
