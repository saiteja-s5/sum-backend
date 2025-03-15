package building.sum.market.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooFinanceHistoricalResponseResult {

	private YahooFinanceHistoricalResponseMeta meta;
	private List<Long> timestamp;
	private YahooFinanceHistoricalResponseIndicators indicators;

}
