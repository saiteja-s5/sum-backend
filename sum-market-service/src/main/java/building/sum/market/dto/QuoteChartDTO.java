package building.sum.market.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import building.sum.market.model.Market;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuoteChartDTO {

	private Map<LocalDateTime, BigDecimal> chart;
	private String symbol;
	private Market market;
	private BigDecimal highestValue;
	private LocalDateTime highestValueOn;
	private BigDecimal lowestValue;
	private LocalDateTime lowestValueOn;
	private Long noOfDataPoints;

	@Override
	public String toString() {
		return chart + "," + symbol + "," + market + "," + highestValue + "," + highestValueOn + "," + lowestValue + ","
				+ lowestValueOn + "," + noOfDataPoints;
	}

}
