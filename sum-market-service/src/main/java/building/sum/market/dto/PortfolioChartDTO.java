package building.sum.market.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PortfolioChartDTO {

	private Map<LocalDateTime, BigDecimal> chart;
	private List<OpenStockDashboardRowDTO> openStocks;
	private BigDecimal highestValue;
	private LocalDateTime highestValueOn;
	private BigDecimal lowestValue;
	private LocalDateTime lowestValueOn;
	private Long noOfDataPoints;

	@Override
	public String toString() {
		return chart + "," + openStocks + "," + highestValue + "," + highestValueOn + "," + lowestValue + ","
				+ lowestValueOn + "," + noOfDataPoints;
	}

}
