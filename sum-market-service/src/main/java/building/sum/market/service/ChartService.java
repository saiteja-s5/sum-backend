package building.sum.market.service;

import building.sum.market.dto.FundChartDTO;
import building.sum.market.dto.PortfolioChartDTO;
import building.sum.market.dto.QuoteChartDTO;
import building.sum.market.model.Market;

public interface ChartService {

	QuoteChartDTO getQuoteChart(Market market, String symbol);

	PortfolioChartDTO getPortfolioChart(String userJoinKey);

	FundChartDTO getFundChart(String userJoinKey);

}
