package building.sum.market.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooFinanceResponseInnerDTO {

	private List<YahooQuoteDTO> result;
	private Object error;

}
