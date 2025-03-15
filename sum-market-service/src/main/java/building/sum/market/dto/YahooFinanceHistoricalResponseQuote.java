package building.sum.market.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooFinanceHistoricalResponseQuote {

	private List<BigDecimal> close;
	private List<BigDecimal> low;
	private List<Long> volume;
	private List<BigDecimal> open;
	private List<BigDecimal> high;

}
