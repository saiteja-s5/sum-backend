package building.sum.market.service;

import building.sum.market.dto.StockDashboardDTO;

public interface DashboardService {

	StockDashboardDTO getOpenStockHoldings(String userJoinkey);

}
