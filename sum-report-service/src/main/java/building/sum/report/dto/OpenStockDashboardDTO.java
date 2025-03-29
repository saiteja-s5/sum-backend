package building.sum.report.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OpenStockDashboardDTO {

	private List<OpenStockDashboardRowDTO> openStocks;
	private BigDecimal totalStockInvestmentValue;
	private BigDecimal totalStockCurrentValue;
	private BigDecimal totalStockCurrentReturn;
	private BigDecimal totalStockCurrentReturnPercent;
	private LocalDateTime openStockLastTransactionOn;
	private LocalDateTime openStockTableUpdatedOn;

	@Override
	public String toString() {
		return openStocks + "," + totalStockInvestmentValue + "," + totalStockCurrentValue + ","
				+ totalStockCurrentReturn + "," + totalStockCurrentReturnPercent + "," + openStockLastTransactionOn
				+ "," + openStockTableUpdatedOn;
	}

}
