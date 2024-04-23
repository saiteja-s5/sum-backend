package building.sum.market.service.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.market.dto.StockDashboardDTO;
import building.sum.market.dto.StockDashboardRowDTO;
import building.sum.market.exception.ResourceNotFoundException;
import building.sum.market.repository.StockRepository;
import building.sum.market.service.DailyMarketService;
import building.sum.market.service.DashboardService;
import building.sum.market.utility.SumUtility;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

	private static final Logger log = LogManager.getLogger();

	private final StockRepository stockRepository;

	private final DailyMarketService marketService;

	// TODO Stock Table last updated on
	@Override
	public StockDashboardDTO getCurrentHoldings() {
		try {
			List<StockDashboardRowDTO> stocks = stockRepository.findAll().stream().map(StockDashboardRowDTO::new)
					.toList();
			if (!stocks.isEmpty()) {
				double totalInvestmentValue = stocks.stream().map(stock -> stock.getBuyValue().doubleValue())
						.reduce(0.0, (v1, v2) -> v1 + v2);
				double currentValue = stocks.stream()
						.map(stock -> marketService.getQuote(stock.getMarket(), stock.getStockSymbol())
								.getRegularMarketPrice().doubleValue() * stock.getQuantity())
						.reduce(0.0, (p1, p2) -> p1 + p2);
				double currentReturn = currentValue - totalInvestmentValue;
				BigDecimal currentReturnPercentage = SumUtility.getPercentageReturn(totalInvestmentValue, currentValue);
				return StockDashboardDTO.builder().stocks(stocks)
						.totalStockInvestmentValue(BigDecimal.valueOf(totalInvestmentValue))
						.totalStockCurrentValue(BigDecimal.valueOf(currentValue))
						.totalStockCurrentReturn(BigDecimal.valueOf(currentReturn))
						.totalStockCurrentReturnPercent(currentReturnPercentage)
						.totalStockOnePercentTargetValue(BigDecimal
								.valueOf(stocks.stream().map(stock -> stock.getOnePercentTarget().doubleValue())
										.reduce(0.0, (v1, v2) -> v1 + v2)))
						.totalStockTwoPercentTargetValue(BigDecimal
								.valueOf(stocks.stream().map(stock -> stock.getTwoPercentTarget().doubleValue())
										.reduce(0.0, (v1, v2) -> v1 + v2)))
						.stockLastTransactionOn(stocks.stream()
								.max(Comparator.comparing(StockDashboardRowDTO::getBuyDate)).get().getBuyDate())
						.stockTableUpdatedOn(null).build();
			} else {
				log.warn("No stocks found");
				return StockDashboardDTO.builder().build();
			}
		} catch (Exception e) {
			log.error("Unable to fetch stocks");
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

}
