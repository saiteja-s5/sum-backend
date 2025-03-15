package building.sum.market.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooFinanceHistoricalResponseAdjustedClose {

	private List<BigDecimal> adjclose;

}
