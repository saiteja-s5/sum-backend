package building.sum.market.service;

import building.sum.market.dto.ClosedStockDashboardDTO;
import building.sum.market.dto.DividendDashboardDTO;
import building.sum.market.dto.FundDashboardDTO;
import building.sum.market.dto.OpenStockDashboardDTO;

public interface DashboardService {

	OpenStockDashboardDTO getOpenStockHoldings(String userJoinkey);

	FundDashboardDTO getFunds(String userJoinKey);

	DividendDashboardDTO getDividends(String userJoinKey);

	ClosedStockDashboardDTO getClosedStockHoldings(String userJoinKey);

}
