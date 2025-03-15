package building.sum.market.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.market.dto.StockDashboardDTO;
import building.sum.market.dto.StockDashboardRowDTO;
import building.sum.market.exception.ResourceNotFoundException;
import building.sum.market.repository.OpenStockRepository;
import building.sum.market.service.DashboardService;
import building.sum.market.service.MarketService;
import building.sum.market.utility.SumUtility;

@Service
public class DashboardServiceImpl implements DashboardService {

	private static final Logger log = LogManager.getLogger();

	private OpenStockRepository openStockRepository;

	private MarketService marketService;

	public DashboardServiceImpl(OpenStockRepository openStockRepository, MarketService marketService) {
		this.openStockRepository = openStockRepository;
		this.marketService = marketService;
	}

	@Override
	public StockDashboardDTO getOpenStockHoldings(String userJoinkey) {
		log.debug(">>>>> getOpenStockHoldings args - {}", userJoinkey);
		try {
			List<StockDashboardRowDTO> stocks = openStockRepository.findAllByUserJoinKey(userJoinkey).stream()
					.map(StockDashboardRowDTO::new).toList();
			if (!stocks.isEmpty()) {
				double totalInvestmentValue = stocks.stream().map(stock -> stock.getBuyValue().doubleValue())
						.reduce(0.0, (v1, v2) -> v1 + v2);
				double currentValue = stocks.stream()
						.map(stock -> marketService.getQuote(stock.getMarket(), stock.getStockSymbol())
								.getRegularMarketPrice().doubleValue() * stock.getQuantity())
						.reduce(0.0, (p1, p2) -> p1 + p2);
				double currentReturn = currentValue - totalInvestmentValue;
				BigDecimal currentReturnPercentage = SumUtility.getPercentageReturn(totalInvestmentValue, currentValue);
				Optional<StockDashboardRowDTO> latestBuyDateContainer = stocks.stream()
						.max(Comparator.comparing(StockDashboardRowDTO::getBuyDate));
				return StockDashboardDTO.builder().stocks(stocks)
						.totalStockInvestmentValue(SumUtility.roundTo(totalInvestmentValue, 2))
						.totalStockCurrentValue(SumUtility.roundTo(currentValue, 2))
						.totalStockCurrentReturn(SumUtility.roundTo(currentReturn, 2))
						.totalStockCurrentReturnPercent(currentReturnPercentage)
						.totalStockOnePercentTargetValue(SumUtility
								.roundTo(stocks.stream().map(stock -> stock.getOnePercentTarget().doubleValue())
										.reduce(0.0, (v1, v2) -> v1 + v2), 2))
						.totalStockTwoPercentTargetValue(SumUtility
								.roundTo(stocks.stream().map(stock -> stock.getTwoPercentTarget().doubleValue())
										.reduce(0.0, (v1, v2) -> v1 + v2), 2))
						.stockLastTransactionOn(
								latestBuyDateContainer.isPresent() ? latestBuyDateContainer.get().getBuyDate() : null)
						.stockTableUpdatedOn(LocalDateTime.now()).build();
			} else {
				log.warn("No stocks found for user - {}", userJoinkey);
				return StockDashboardDTO.builder().build();
			}
		} catch (Exception e) {
			log.error("Unable to fetch stocks for user - {}", userJoinkey);
			throw new ResourceNotFoundException(e.getMessage());
		} finally {
			log.debug("<<<<< getOpenStockHoldings args - {}", userJoinkey);
		}
	}

}
