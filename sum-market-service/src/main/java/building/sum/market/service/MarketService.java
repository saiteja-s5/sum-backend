package building.sum.market.service;

import building.sum.market.dto.YahooFinanceHistoricalResponseDTO;
import building.sum.market.dto.YahooQuoteDTO;
import building.sum.market.model.Market;

public interface MarketService {

	YahooQuoteDTO getQuote(Market market, String symbol);

	YahooFinanceHistoricalResponseDTO getHistoricalStockQuote(Market market, String symbol, String from, String to);

	void saveHistoricalStockQuote(String to);

	void updateStockUpdatedDates();

}
