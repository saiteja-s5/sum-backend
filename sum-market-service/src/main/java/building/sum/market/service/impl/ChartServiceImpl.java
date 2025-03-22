package building.sum.market.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.market.dto.FundChartDTO;
import building.sum.market.dto.FundDashboardRowDTO;
import building.sum.market.dto.OpenStockDashboardRowDTO;
import building.sum.market.dto.PortfolioChartDTO;
import building.sum.market.dto.QuoteChartDTO;
import building.sum.market.model.ClosedStock;
import building.sum.market.model.Fund;
import building.sum.market.model.HistoricalDataBSE2021To2025;
import building.sum.market.model.HistoricalDataNSE2021To2025;
import building.sum.market.model.Market;
import building.sum.market.model.OpenStock;
import building.sum.market.repository.ClosedStockRepository;
import building.sum.market.repository.FundRepository;
import building.sum.market.repository.HistoricalDataBSE2021To2025Repository;
import building.sum.market.repository.HistoricalDataNSE2021To2025Repository;
import building.sum.market.repository.OpenStockRepository;
import building.sum.market.service.ChartService;
import building.sum.market.utility.SumUtility;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChartServiceImpl implements ChartService {

	private static final Logger log = LogManager.getLogger();

	private static final LocalDateTime START_DATE = SumUtility.HISTORICAL_DATE_START_2021;

	private final HistoricalDataNSE2021To2025Repository historicalDataNSE2021To2025Repository;

	private final HistoricalDataBSE2021To2025Repository historicalDataBSE2021To2025Repository;

	private final OpenStockRepository openStockRepository;

	private final ClosedStockRepository closedStockRepository;

	private final FundRepository fundRepository;

	@Override
	public QuoteChartDTO getQuoteChart(Market market, String symbol) {
		log.debug(">>>>> getQuoteChart args - {}, {}", market, symbol);
		Map<LocalDateTime, BigDecimal> chart = new LinkedHashMap<>();
		BigDecimal highestValue = null;
		LocalDateTime highestValueOn = null;
		BigDecimal lowestValue = null;
		LocalDateTime lowestValueOn = null;
		if (market.equals(Market.NSE)) {
			List<HistoricalDataNSE2021To2025> historicalData = historicalDataNSE2021To2025Repository
					.findBySymbolOrderByTradedDateAsc(symbol);
			if (!historicalData.isEmpty()) {
				highestValue = historicalData.get(0).getHigh();
				highestValueOn = historicalData.get(0).getTradedDate();
				lowestValue = historicalData.get(0).getLow();
				lowestValueOn = historicalData.get(0).getTradedDate();
			}
			for (HistoricalDataNSE2021To2025 data : historicalData) {
				chart.put(data.getTradedDate(), data.getAdjustedClose());
				if (data.getHigh() != null && highestValue != null) {
					if (data.getHigh().doubleValue() >= highestValue.doubleValue()) {
						highestValue = data.getHigh();
						highestValueOn = data.getTradedDate();
					}
				}
				if (data.getLow() != null && lowestValue != null) {
					if (data.getLow().doubleValue() <= lowestValue.doubleValue()) {
						lowestValue = data.getLow();
						lowestValueOn = data.getTradedDate();
					}
				}
			}
		} else if (market.equals(Market.BSE)) {
			List<HistoricalDataBSE2021To2025> historicalData = historicalDataBSE2021To2025Repository
					.findBySymbolOrderByTradedDateAsc(symbol);
			if (!historicalData.isEmpty()) {
				highestValue = historicalData.get(0).getHigh();
				highestValueOn = historicalData.get(0).getTradedDate();
				lowestValue = historicalData.get(0).getLow();
				lowestValueOn = historicalData.get(0).getTradedDate();
			}
			for (HistoricalDataBSE2021To2025 data : historicalData) {
				chart.put(data.getTradedDate(), data.getAdjustedClose());
				if (data.getHigh() != null && highestValue != null) {
					if (data.getHigh().doubleValue() >= highestValue.doubleValue()) {
						highestValue = data.getHigh();
						highestValueOn = data.getTradedDate();
					}
				}
				if (data.getLow() != null && lowestValue != null) {
					if (data.getLow().doubleValue() <= lowestValue.doubleValue()) {
						lowestValue = data.getLow();
						lowestValueOn = data.getTradedDate();
					}
				}
			}
		}
		log.debug("<<<<< getQuoteChart args - {}, {}", market, symbol);
		return QuoteChartDTO.builder().chart(chart).symbol(symbol).market(market).highestValue(highestValue)
				.highestValueOn(highestValueOn).lowestValue(lowestValue).lowestValueOn(lowestValueOn)
				.noOfDataPoints(Long.valueOf(chart.size())).build();
	}

	// TODO
	public PortfolioChartDTO getPortfolioChartN(String userJoinKey) {
		log.debug(">>>>> getPortfolioChart args - {}", userJoinKey);
		Map<LocalDateTime, BigDecimal> portfolioChart = new TreeMap<>();
		Map<String, Map<LocalDateTime, BigDecimal>> allCharts = new HashMap<>();
		List<OpenStock> openStocks = openStockRepository.findAllByUserJoinKey(userJoinKey);
		for (OpenStock openStock : openStocks) {
			if (openStock.getBoughtMarket().equals(Market.NSE)) {
				List<HistoricalDataNSE2021To2025> historicalData = historicalDataNSE2021To2025Repository
						.findBySymbolAndTradedDateGreaterThanEqualOrderByTradedDateAsc(openStock.getStockSymbol(),
								openStock.getInvestmentDate());
				LinkedHashMap<LocalDateTime, BigDecimal> chart = historicalData.stream()
						.collect(Collectors.toMap(HistoricalDataNSE2021To2025::getTradedDate,
								close -> close.getAdjustedClose().multiply(BigDecimal.valueOf(openStock.getQuantity())),
								(existing, replacement) -> existing, LinkedHashMap::new));
				allCharts.put(openStock.getStockSymbol(), chart);
			} else if (openStock.getBoughtMarket().equals(Market.BSE)) {
				List<HistoricalDataBSE2021To2025> historicalData = historicalDataBSE2021To2025Repository
						.findBySymbolAndTradedDateGreaterThanEqualOrderByTradedDateAsc(openStock.getStockSymbol(),
								openStock.getInvestmentDate());
				LinkedHashMap<LocalDateTime, BigDecimal> chart = historicalData.stream()
						.collect(Collectors.toMap(HistoricalDataBSE2021To2025::getTradedDate,
								close -> close.getAdjustedClose().multiply(BigDecimal.valueOf(openStock.getQuantity())),
								(existing, replacement) -> existing, LinkedHashMap::new));
				allCharts.put(openStock.getStockSymbol(), chart);
			}
		}
		for (Map.Entry<String, Map<LocalDateTime, BigDecimal>> allChartsEntry : allCharts.entrySet()) {
			for (Map.Entry<LocalDateTime, BigDecimal> chartEntry : allChartsEntry.getValue().entrySet()) {
				if (portfolioChart.containsKey(chartEntry.getKey())) {
					BigDecimal oldValue = portfolioChart.get(chartEntry.getKey());
					portfolioChart.put(chartEntry.getKey(), oldValue.add(chartEntry.getValue()));
				} else {
					portfolioChart.put(chartEntry.getKey(), chartEntry.getValue());
				}
			}
		}
		Entry<LocalDateTime, BigDecimal> max = portfolioChart.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getValue)).orElse(null);
		Entry<LocalDateTime, BigDecimal> min = portfolioChart.entrySet().stream()
				.min(Comparator.comparing(Map.Entry::getValue)).orElse(null);
		BigDecimal highestValue = max != null ? max.getValue() : null;
		LocalDateTime highestValueOn = max != null ? max.getKey() : null;
		BigDecimal lowestValue = min != null ? min.getValue() : null;
		LocalDateTime lowestValueOn = min != null ? min.getKey() : null;
		log.debug("<<<<< getPortfolioChart args - {}", userJoinKey);
		return PortfolioChartDTO.builder().chart(portfolioChart)
				.openStocks(openStocks.stream().map(OpenStockDashboardRowDTO::new).toList()).highestValue(highestValue)
				.highestValueOn(highestValueOn).lowestValue(lowestValue).lowestValueOn(lowestValueOn)
				.noOfDataPoints(Long.valueOf(portfolioChart.size())).build();
	}

	@Override
	public FundChartDTO getFundChart(String userJoinKey) {
		log.debug(">>>>> getFundChart args - {}", userJoinKey);
		List<Fund> funds = fundRepository.findAllByUserJoinKey(userJoinKey);
		Map<LocalDateTime, BigDecimal> chart = funds.stream()
				.sorted((f1, f2) -> f1.getTransactionDate().compareTo(f2.getTransactionDate()))
				.collect(Collectors.toMap(Fund::getTransactionDate,
						fund -> fund.getCreditedAmount().subtract(fund.getDebitedAmount()), BigDecimal::add,
						TreeMap::new));
		LocalDateTime startDate = chart.entrySet().iterator().next().getKey();
		Map<LocalDateTime, BigDecimal> fundChart = generateDatesVsZerosMap(startDate);
		Map.Entry<LocalDateTime, BigDecimal> previousEntry = null;
		for (Map.Entry<LocalDateTime, BigDecimal> entry : fundChart.entrySet()) {
			if (previousEntry != null) {
				if (chart.containsKey(entry.getKey())) {
					fundChart.put(entry.getKey(), previousEntry.getValue().add(chart.get(entry.getKey())));
				} else {
					fundChart.put(entry.getKey(), previousEntry.getValue());
				}
			} else {
				fundChart.put(entry.getKey(), chart.get(entry.getKey()));
			}
			previousEntry = entry;
		}
		Optional<Fund> highestCreditedEntry = funds.stream().max(Comparator.comparing(Fund::getCreditedAmount));
		Optional<Fund> highestDebitedEntry = funds.stream().max(Comparator.comparing(Fund::getDebitedAmount));
		log.debug("<<<<< getFundChart args - {}", userJoinKey);
		return FundChartDTO.builder().chart(fundChart).funds(funds.stream().map(FundDashboardRowDTO::new).toList())
				.highestCreditedValue(
						highestCreditedEntry.isPresent() ? highestCreditedEntry.get().getCreditedAmount() : null)
				.highestCreditedValueOn(
						highestCreditedEntry.isPresent() ? highestCreditedEntry.get().getTransactionDate() : null)
				.highestDebitedValue(
						highestDebitedEntry.isPresent() ? highestDebitedEntry.get().getDebitedAmount() : null)
				.highestDebitedValueOn(
						highestDebitedEntry.isPresent() ? highestDebitedEntry.get().getTransactionDate() : null)
				.noOfDataPoints(Long.valueOf(fundChart.size())).build();
	}

	// TODO
	@Override
	public PortfolioChartDTO getPortfolioChart(String userJoinKey) {
		log.debug(">>>>> getPortfolioChart args - {}", userJoinKey);
		Map<String, Map<LocalDateTime, BigDecimal>> allCharts = new HashMap<>();
		List<OpenStock> openStocks = openStockRepository.findAllByUserJoinKey(userJoinKey);
		log.info(openStocks);
		for (OpenStock openStock : openStocks) {
			if (openStock.getBoughtMarket().equals(Market.NSE)) {
				List<HistoricalDataNSE2021To2025> historicalData = historicalDataNSE2021To2025Repository
						.findBySymbolAndTradedDateGreaterThanEqualOrderByTradedDateAsc(openStock.getStockSymbol(),
								openStock.getInvestmentDate());
				LinkedHashMap<LocalDateTime, BigDecimal> chart = historicalData.stream()
						.collect(Collectors.toMap(HistoricalDataNSE2021To2025::getTradedDate,
								close -> close.getAdjustedClose().multiply(BigDecimal.valueOf(openStock.getQuantity())),
								(existing, replacement) -> existing, LinkedHashMap::new));
				allCharts.put(openStock.getStockSymbol(), chart);
			} else if (openStock.getBoughtMarket().equals(Market.BSE)) {
				List<HistoricalDataBSE2021To2025> historicalData = historicalDataBSE2021To2025Repository
						.findBySymbolAndTradedDateGreaterThanEqualOrderByTradedDateAsc(openStock.getStockSymbol(),
								openStock.getInvestmentDate());
				LinkedHashMap<LocalDateTime, BigDecimal> chart = historicalData.stream()
						.collect(Collectors.toMap(HistoricalDataBSE2021To2025::getTradedDate,
								close -> close.getAdjustedClose().multiply(BigDecimal.valueOf(openStock.getQuantity())),
								(existing, replacement) -> existing, LinkedHashMap::new));
				allCharts.put(openStock.getStockSymbol(), chart);
			}
		}
		log.info(allCharts);
		List<ClosedStock> closedStocks = closedStockRepository.findAllByUserJoinKey(userJoinKey).stream()
				.filter(closed -> closed.getBuyDate() != null
						&& closed.getBuyDate().isAfter(LocalDateTime.of(2024, 1, 1, 0, 0)))
				.toList();
		log.info(closedStocks);
		for (ClosedStock closedStock : closedStocks) {
			if (closedStock.getBoughtMarket().equals(Market.NSE)) {
				List<HistoricalDataNSE2021To2025> historicalData = historicalDataNSE2021To2025Repository
						.findBySymbolAndTradedDateBetweenOrderByTradedDateAsc(closedStock.getStockSymbol(),
								LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.now());
				LinkedHashMap<LocalDateTime, BigDecimal> chart = historicalData.stream()
						.filter(closed -> closed.getAdjustedClose() != null
								&& closed.getTradedDate().isAfter(LocalDateTime.of(2024, 1, 1, 0, 0)))
						.collect(Collectors.toMap(HistoricalDataNSE2021To2025::getTradedDate,
								close -> close.getAdjustedClose()
										.multiply(BigDecimal.valueOf(closedStock.getBuyQuantity())),
								(existing, replacement) -> existing, LinkedHashMap::new));
				allCharts.put(closedStock.getStockSymbol(), chart);
			} else if (closedStock.getBoughtMarket().equals(Market.BSE)) {
				List<HistoricalDataBSE2021To2025> historicalData = historicalDataBSE2021To2025Repository
						.findBySymbolAndTradedDateBetweenOrderByTradedDateAsc(closedStock.getStockSymbol(),
								LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.now());
				LinkedHashMap<LocalDateTime, BigDecimal> chart = historicalData.stream()
						.filter(closed -> closed.getAdjustedClose() != null
								&& closed.getTradedDate().isAfter(LocalDateTime.of(2024, 1, 1, 0, 0)))
						.collect(Collectors.toMap(HistoricalDataBSE2021To2025::getTradedDate,
								close -> close.getAdjustedClose()
										.multiply(BigDecimal.valueOf(closedStock.getBuyQuantity())),
								(existing, replacement) -> existing, LinkedHashMap::new));
				allCharts.put(closedStock.getStockSymbol(), chart);
			}
		}
		Map<LocalDateTime, BigDecimal> portfolioChart = generateDatesVsZerosMap(
				closedStocks.stream().min(Comparator.comparing(ClosedStock::getBuyDate)).get().getBuyDate());
		log.info(allCharts);
//		for (Map.Entry<String, Map<LocalDateTime, BigDecimal>> allChartsEntry : allCharts.entrySet()) {
//			for (Map.Entry<LocalDateTime, BigDecimal> chartEntry : allChartsEntry.getValue().entrySet()) {
//				if (portfolioChart.containsKey(chartEntry.getKey())) {
//					BigDecimal oldValue = portfolioChart.get(chartEntry.getKey());
//					portfolioChart.put(chartEntry.getKey(), oldValue.add(chartEntry.getValue()));
//				} else {
//					portfolioChart.put(chartEntry.getKey(), chartEntry.getValue());
//				}
//			}
//		}
		Map<LocalDateTime, BigDecimal> chart = new TreeMap<>();
		for (Map.Entry<String, Map<LocalDateTime, BigDecimal>> entry1 : allCharts.entrySet()) {
			for (Map.Entry<LocalDateTime, BigDecimal> entry : entry1.getValue().entrySet()) {
				if (chart.containsKey(entry.getKey())) {
					chart.put(entry.getKey(), entry.getValue().add(chart.get(entry.getKey())));
				} else {
					chart.put(entry.getKey(), entry.getValue());
				}
			}
		}
		log.info(chart);
		Map.Entry<LocalDateTime, BigDecimal> previousEntry = null;
		for (Map.Entry<LocalDateTime, BigDecimal> entry : portfolioChart.entrySet()) {
			if (previousEntry != null) {
				if (chart.containsKey(entry.getKey())) {
					portfolioChart.put(entry.getKey(), previousEntry.getValue().add(chart.get(entry.getKey())));
				} else {
					portfolioChart.put(entry.getKey(), previousEntry.getValue());
				}
			} else {
				portfolioChart.put(entry.getKey(), chart.get(entry.getKey()));
			}
			previousEntry = entry;
		}
		Entry<LocalDateTime, BigDecimal> max = portfolioChart.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getValue)).orElse(null);
		Entry<LocalDateTime, BigDecimal> min = portfolioChart.entrySet().stream()
				.min(Comparator.comparing(Map.Entry::getValue)).orElse(null);
		BigDecimal highestValue = max != null ? max.getValue() : null;
		LocalDateTime highestValueOn = max != null ? max.getKey() : null;
		BigDecimal lowestValue = min != null ? min.getValue() : null;
		LocalDateTime lowestValueOn = min != null ? min.getKey() : null;
		log.debug("<<<<< getPortfolioChart args - {}", userJoinKey);
		return PortfolioChartDTO.builder().chart(portfolioChart)
				.openStocks(openStocks.stream().map(OpenStockDashboardRowDTO::new).toList()).highestValue(highestValue)
				.highestValueOn(highestValueOn).lowestValue(lowestValue).lowestValueOn(lowestValueOn)
				.noOfDataPoints(Long.valueOf(portfolioChart.size())).build();
	}

	private Map<LocalDateTime, BigDecimal> generateDatesVsZerosMap(LocalDateTime... startDate) {
		Map<LocalDateTime, BigDecimal> chart = new LinkedHashMap<>();
		LocalDateTime startDateForLoop;
		if (startDate.length == 0) {
			startDateForLoop = START_DATE;
		} else {
			startDateForLoop = startDate[0];
		}
		for (LocalDateTime i = startDateForLoop; i.isBefore(LocalDateTime.now()); i = i.plusDays(1)) {
			chart.put(i, BigDecimal.ZERO);
		}
		return chart;
	}

}
