package building.sum.market.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooFinanceHistoricalResponseChart {
	
	private List<YahooFinanceHistoricalResponseResult> result;
	private String error;

}
