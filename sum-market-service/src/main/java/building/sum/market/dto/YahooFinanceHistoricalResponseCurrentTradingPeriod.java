package building.sum.market.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooFinanceHistoricalResponseCurrentTradingPeriod {

	private YahooFinanceHistoricalResponseTradingPeriod pre;
	private YahooFinanceHistoricalResponseTradingPeriod regular;
	private YahooFinanceHistoricalResponseTradingPeriod post;

}
