package building.sum.market.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooFinanceHistoricalResponseTradingPeriod {
	
	private String timezone;
	private Long start;
	private Long end;
	private Integer gmtoffset;
	
}
