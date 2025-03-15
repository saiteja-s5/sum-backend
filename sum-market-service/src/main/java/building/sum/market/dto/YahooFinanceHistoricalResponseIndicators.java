package building.sum.market.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooFinanceHistoricalResponseIndicators {

	private List<YahooFinanceHistoricalResponseQuote> quote;
	private List<YahooFinanceHistoricalResponseAdjustedClose> adjclose;

}
