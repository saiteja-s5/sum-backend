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
public class FundChartDTO {

	private Map<LocalDateTime, BigDecimal> chart;
	private List<FundDashboardRowDTO> funds;
	private BigDecimal highestCreditedValue;
	private LocalDateTime highestCreditedValueOn;
	private BigDecimal highestDebitedValue;
	private LocalDateTime highestDebitedValueOn;
	private Long noOfDataPoints;

	@Override
	public String toString() {
		return chart + "," + funds + "," + highestCreditedValue + "," + highestCreditedValueOn + ","
				+ highestDebitedValue + "," + highestDebitedValueOn + "," + noOfDataPoints;
	}

}
