package building.sum.market.service;

import building.sum.market.dto.YahooQuoteDTO;
import building.sum.market.model.Market;

public interface DailyMarketService {

	YahooQuoteDTO getQuote(Market market, String symbol);

}
