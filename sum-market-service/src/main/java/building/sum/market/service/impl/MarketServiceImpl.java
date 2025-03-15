package building.sum.market.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import building.sum.market.dto.YahooFinanceHistoricalResponseAdjustedClose;
import building.sum.market.dto.YahooFinanceHistoricalResponseDTO;
import building.sum.market.dto.YahooFinanceHistoricalResponseIndicators;
import building.sum.market.dto.YahooFinanceHistoricalResponseQuote;
import building.sum.market.dto.YahooFinanceHistoricalResponseResult;
import building.sum.market.dto.YahooFinanceResponseDTO;
import building.sum.market.dto.YahooQuoteDTO;
import building.sum.market.exception.ResourceNotFetchedException;
import building.sum.market.model.Company;
import building.sum.market.model.HistoricalDataBSE2021To2025;
import building.sum.market.model.HistoricalDataNSE2021To2025;
import building.sum.market.model.HistoricalDataUpdatedDate;
import building.sum.market.model.Market;
import building.sum.market.model.YahooFinanceApiRequestProperties;
import building.sum.market.repository.CompanyRepository;
import building.sum.market.repository.HistoricalDataBSE2021To2025Repository;
import building.sum.market.repository.HistoricalDataNSE2021To2025Repository;
import building.sum.market.repository.HistoricalDataUpdatedDateRepository;
import building.sum.market.service.MarketService;
import building.sum.market.utility.SumUtility;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MarketServiceImpl implements MarketService {

	private static final Logger log = LogManager.getLogger();

	private final YahooFinanceApiRequestProperties requestProperties;

	private final WebClient.Builder webClientBuilder;

	private final HistoricalDataNSE2021To2025Repository historicalDataNSE2021To2025Repository;

	private final HistoricalDataBSE2021To2025Repository historicalDataBSE2021To2025Repository;

	private final CompanyRepository companyRepository;

	private final HistoricalDataUpdatedDateRepository historicalDataUpdatedDateRepository;

	private static final DateTimeFormatter DMY_FORMATTER = SumUtility.DMY_FORMATTER;

	private static final Integer IS_ACTIVE = SumUtility.IS_ACTIVE;

	private static final LocalDateTime START_2025 = SumUtility.HISTORICAL_DATE_START_2021;

	@Override
	public YahooQuoteDTO getQuote(Market market, String symbol) {
		log.debug(">>>>> getQuote args - {}, {}", symbol, market);
		URIBuilder uriBuilder = new URIBuilder().setScheme(requestProperties.getScheme())
				.setHost(requestProperties.getHost()).setPath(requestProperties.getQuotePath())
				.addParameter("symbols", symbol + market.getExtension())
				.addParameter("crumb", requestProperties.getCrumb());
		try {
			log.info("Yahoo Finance URL - {}", uriBuilder.build());
			YahooFinanceResponseDTO response = webClientBuilder.build().get().uri(uriBuilder.build())
					.header("Cookie", requestProperties.getCookie()).retrieve()
					.bodyToMono(YahooFinanceResponseDTO.class).block();
			YahooQuoteDTO fetchedResponse;
			if (response != null && response.getQuoteResponse() != null
					&& !response.getQuoteResponse().getResult().isEmpty()) {
				fetchedResponse = response.getQuoteResponse().getResult().get(0);
				log.debug("Response mapping to DTO is completed for symbol - {}", symbol);
				return fetchedResponse;
			} else {
				log.warn("Exception occurred while fetching stock - {}", symbol);
				throw new ResourceNotFetchedException(String.format("Quote - %s not fetched", symbol));
			}
		} catch (Exception e) {
			throw new ResourceNotFetchedException(e.getMessage());
		} finally {
			log.debug("<<<<< getQuote args - {}, {}", symbol, market);
		}
	}

	@Override
	public YahooFinanceHistoricalResponseDTO getHistoricalStockQuote(Market market, String symbol, String from,
			String to) {
		log.debug(">>>>> getHistoricalStockQuote args - {}, {}, {}, {}", market, symbol, from, to);
		URIBuilder uriBuilder = new URIBuilder().setScheme(requestProperties.getScheme())
				.setHost(requestProperties.getHost())
				.setPath(requestProperties.getHistoricalQuotePath() + symbol + market.getExtension())
				.addParameter("crumb", requestProperties.getCrumb()).addParameter("includeAdjustedClose", "true")
				.addParameter("interval", "1d")
				.addParameter("period1", String.valueOf(localDateToEpochSecond(stringToLocalDate(from, DMY_FORMATTER))))
				.addParameter("period2", String.valueOf(localDateToEpochSecond(stringToLocalDate(to, DMY_FORMATTER))));
		try {
			log.debug("Yahoo Finance URL - {}", uriBuilder.build());
			YahooFinanceHistoricalResponseDTO response = webClientBuilder.build().get().uri(uriBuilder.build())
					.header("Cookie", requestProperties.getCookie()).retrieve()
					.bodyToMono(YahooFinanceHistoricalResponseDTO.class).block();
			if (response != null) {
				return response;
			} else {
				log.warn("Exception occurred while fetching stock - {}", symbol);
				throw new ResourceNotFetchedException(String.format("Quote - %s not fetched", symbol));
			}
		} catch (Exception e) {
			throw new ResourceNotFetchedException(e.getMessage());
		} finally {
			log.debug("<<<<< getHistoricalStockQuote args - {}, {}, {}, {}", market, symbol, from, to);
		}
	}

	@Override
	public void saveHistoricalStockQuote(String from, String to) {
		log.debug(">>>>> saveHistoricalStockQuote args - {}, {}", from, to);
		List<String> skippedStocks = new ArrayList<>();
		for (Company company : companyRepository.findByIsActive(IS_ACTIVE).stream()
				.filter(company -> company.getSymbol() != null && company.getMarket() != null).toList()) {
			log.debug("Saving historical data for Company - {} and Market - {}", company.getSymbol(),
					company.getMarket());
			try {
				Optional<HistoricalDataUpdatedDate> lastDateContainer = historicalDataUpdatedDateRepository
						.findByStockSymbolAndMarket(company.getSymbol(), company.getMarket());
				HistoricalDataUpdatedDate historicalDataUpdatedDate;
				String newFromDate;
				if (lastDateContainer.isPresent()) {
					historicalDataUpdatedDate = lastDateContainer.get();
					newFromDate = historicalDataUpdatedDate.getStockUpdatedDateTime().plusDays(1).format(DMY_FORMATTER);
				} else {
					newFromDate = START_2025.format(DMY_FORMATTER);
				}
				YahooFinanceHistoricalResponseDTO response = getHistoricalStockQuote(company.getMarket(),
						company.getSymbol(), newFromDate, to);
				if (response.getChart() != null && !response.getChart().getResult().isEmpty()) {
					// Save to History Table
					if (company.getMarket().equals(Market.NSE)) {
						historicalDataNSE2021To2025Repository
								.saveAll(yahooDTOtoHistoricalNSEDTO(response.getChart().getResult()).stream()
										.map(HistoricalDataNSE2021To2025.class::cast).toList());
					} else if (company.getMarket().equals(Market.BSE)) {
						historicalDataBSE2021To2025Repository
								.saveAll(yahooDTOtoHistoricalBSEDTO(response.getChart().getResult()).stream()
										.map(HistoricalDataBSE2021To2025.class::cast).toList());
					}
					// Save to Last Updated Table
					Optional<Long> latestQuoteFromYfinance = response.getChart().getResult().get(0).getTimestamp()
							.stream().max(Long::compareTo);
					if (latestQuoteFromYfinance.isPresent()) {
						HistoricalDataUpdatedDate historyUpdatedRecord;
						if (lastDateContainer.isPresent()) {
							historyUpdatedRecord = lastDateContainer.get();
							historyUpdatedRecord.setStockUpdatedDateTime(
									epochSecondToLocalDate(latestQuoteFromYfinance.get().longValue()));
							historyUpdatedRecord.setUpdatedBy("");
							historyUpdatedRecord.setUpdatedDateTime(LocalDateTime.now());
						} else {
							historyUpdatedRecord = HistoricalDataUpdatedDate.builder()
									.stockName(company.getCompanyName()).stockSymbol(company.getSymbol())
									.market(company.getMarket())
									.stockUpdatedDateTime(
											epochSecondToLocalDate(latestQuoteFromYfinance.get().longValue()))
									.createdBy("").createdDateTime(LocalDateTime.now()).updatedBy("")
									.updatedDateTime(LocalDateTime.now()).build();
						}
						historicalDataUpdatedDateRepository.save(historyUpdatedRecord);
					}
				} else {
					log.warn("Response received as null for Company - {}", company.getSymbol());
					throw new ResourceNotFetchedException("Null received from Yahoo Finance API");
				}
			} catch (Exception e) {
				log.warn("Skipped Company - {} and Market - {}", company.getCompanyName(), company.getMarket());
				skippedStocks.add(company.getSymbol() + company.getMarket().getExtension());
			}
		}
		log.info("Skipped - {}", skippedStocks);
		log.debug("<<<<< saveHistoricalStockQuote args - {}, {}", from, to);
	}

	public void updateStockUpdatedDates() {
		log.debug(">>>>> updateStockUpdatedDates");
		for (Company company : companyRepository.findByIsActive(IS_ACTIVE).stream()
				.filter(company -> company.getSymbol() != null && company.getMarket() != null).toList()) {
			log.debug("Saving/Updating last updated date for Company - {} and Market - {}", company.getSymbol(),
					company.getMarket());
			if (company.getMarket().equals(Market.NSE)) {
				Optional<HistoricalDataNSE2021To2025> nseHistoryContainer = historicalDataNSE2021To2025Repository
						.findTopBySymbolOrderByTradedDateDesc(company.getSymbol());
				HistoricalDataUpdatedDate historyUpdatedRecord;
				if (nseHistoryContainer.isPresent()) {
					LocalDateTime lastTradedDate = nseHistoryContainer.get().getTradedDate();
					Optional<HistoricalDataUpdatedDate> historyDataContainer = historicalDataUpdatedDateRepository
							.findByStockSymbolAndMarket(company.getSymbol(), company.getMarket());
					if (historyDataContainer.isPresent()) {
						historyUpdatedRecord = historyDataContainer.get();
						historyUpdatedRecord.setStockUpdatedDateTime(lastTradedDate);
						historyUpdatedRecord.setUpdatedBy("");
						historyUpdatedRecord.setUpdatedDateTime(LocalDateTime.now());
					} else {
						historyUpdatedRecord = HistoricalDataUpdatedDate.builder().stockName(company.getCompanyName())
								.stockSymbol(company.getSymbol()).market(company.getMarket())
								.stockUpdatedDateTime(lastTradedDate).createdBy("").createdDateTime(LocalDateTime.now())
								.updatedBy("").updatedDateTime(LocalDateTime.now()).build();
					}
				} else {
					historyUpdatedRecord = HistoricalDataUpdatedDate.builder().stockName(company.getCompanyName())
							.stockSymbol(company.getSymbol()).market(company.getMarket())
							.stockUpdatedDateTime(START_2025).createdBy("").createdDateTime(LocalDateTime.now())
							.updatedBy("").updatedDateTime(LocalDateTime.now()).build();
				}
				historicalDataUpdatedDateRepository.save(historyUpdatedRecord);
			} else if (company.getMarket().equals(Market.BSE)) {
				Optional<HistoricalDataBSE2021To2025> bseHistoryContainer = historicalDataBSE2021To2025Repository
						.findTopBySymbolOrderByTradedDateDesc(company.getSymbol());
				HistoricalDataUpdatedDate historyUpdatedRecord;
				if (bseHistoryContainer.isPresent()) {
					LocalDateTime lastTradedDate = bseHistoryContainer.get().getTradedDate();
					Optional<HistoricalDataUpdatedDate> historyDataContainer = historicalDataUpdatedDateRepository
							.findByStockSymbolAndMarket(company.getSymbol(), company.getMarket());
					if (historyDataContainer.isPresent()) {
						historyUpdatedRecord = historyDataContainer.get();
						historyUpdatedRecord.setStockUpdatedDateTime(lastTradedDate);
						historyUpdatedRecord.setUpdatedBy("");
						historyUpdatedRecord.setUpdatedDateTime(LocalDateTime.now());
					} else {
						historyUpdatedRecord = HistoricalDataUpdatedDate.builder().stockName(company.getCompanyName())
								.stockSymbol(company.getSymbol()).market(company.getMarket())
								.stockUpdatedDateTime(lastTradedDate).createdBy("").createdDateTime(LocalDateTime.now())
								.updatedBy("").updatedDateTime(LocalDateTime.now()).build();
					}
				} else {
					historyUpdatedRecord = HistoricalDataUpdatedDate.builder().stockName(company.getCompanyName())
							.stockSymbol(company.getSymbol()).market(company.getMarket())
							.stockUpdatedDateTime(START_2025).createdBy("").createdDateTime(LocalDateTime.now())
							.updatedBy("").updatedDateTime(LocalDateTime.now()).build();
				}
				historicalDataUpdatedDateRepository.save(historyUpdatedRecord);
			}
		}
		log.debug("<<<<< updateStockUpdatedDates");
	}

	private long localDateToEpochSecond(LocalDate localDate) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZoneOffset zoneOffSet = zoneId.getRules().getOffset(LocalDateTime.now());
		LocalDate date = localDate;
		LocalTime time = LocalTime.parse("00:00:00");
		return date.toEpochSecond(time, zoneOffSet);
	}

	private LocalDate stringToLocalDate(String date, DateTimeFormatter format) {
		return LocalDate.parse(date, format);
	}

	public LocalDateTime epochSecondToLocalDate(long seconds) {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault());
	}

	private List<HistoricalDataBSE2021To2025> yahooDTOtoHistoricalBSEDTO(
			List<YahooFinanceHistoricalResponseResult> results) {
		List<HistoricalDataBSE2021To2025> history = new ArrayList<>();
		YahooFinanceHistoricalResponseResult intermediateJsonLevel1 = results.get(0);
		YahooFinanceHistoricalResponseIndicators intermediateJsonLevel2 = intermediateJsonLevel1.getIndicators();
		YahooFinanceHistoricalResponseQuote opLoHiClAdjClMap = intermediateJsonLevel2.getQuote().get(0);
		YahooFinanceHistoricalResponseAdjustedClose adjCloseMap = intermediateJsonLevel2.getAdjclose().get(0);
		if (intermediateJsonLevel1.getTimestamp() != null) {
			List<Long> tradedDatesInObject = intermediateJsonLevel1.getTimestamp();
			List<BigDecimal> opens = opLoHiClAdjClMap.getOpen();
			List<BigDecimal> closes = opLoHiClAdjClMap.getClose();
			List<BigDecimal> lows = opLoHiClAdjClMap.getLow();
			List<BigDecimal> highs = opLoHiClAdjClMap.getHigh();
			List<Long> volumes = intermediateJsonLevel2.getQuote().get(0).getVolume();
			List<BigDecimal> adjCloses = adjCloseMap.getAdjclose();
			for (int i = 0; i < tradedDatesInObject.size(); i++) {
				history.add(HistoricalDataBSE2021To2025.builder()
						.symbol(intermediateJsonLevel1.getMeta().getSymbol().substring(0,
								intermediateJsonLevel1.getMeta().getSymbol().length() - 3))
						.tradedDate(epochSecondToLocalDate(tradedDatesInObject.get(i).longValue())).open(opens.get(i))
						.close(closes.get(i)).low(lows.get(i)).high(highs.get(i)).adjustedClose(adjCloses.get(i))
						.volume(volumes.get(i)).createdDateTime(LocalDateTime.now()).build());
			}
		}
		return history;
	}

	private List<HistoricalDataNSE2021To2025> yahooDTOtoHistoricalNSEDTO(
			List<YahooFinanceHistoricalResponseResult> results) {
		List<HistoricalDataNSE2021To2025> history = new ArrayList<>();
		YahooFinanceHistoricalResponseResult intermediateJsonLevel1 = results.get(0);
		YahooFinanceHistoricalResponseIndicators intermediateJsonLevel2 = intermediateJsonLevel1.getIndicators();
		YahooFinanceHistoricalResponseQuote opLoHiClAdjClMap = intermediateJsonLevel2.getQuote().get(0);
		YahooFinanceHistoricalResponseAdjustedClose adjCloseMap = intermediateJsonLevel2.getAdjclose().get(0);
		if (intermediateJsonLevel1.getTimestamp() != null) {
			List<Long> tradedDatesInObject = intermediateJsonLevel1.getTimestamp();
			List<BigDecimal> opens = opLoHiClAdjClMap.getOpen();
			List<BigDecimal> closes = opLoHiClAdjClMap.getClose();
			List<BigDecimal> lows = opLoHiClAdjClMap.getLow();
			List<BigDecimal> highs = opLoHiClAdjClMap.getHigh();
			List<Long> volumes = intermediateJsonLevel2.getQuote().get(0).getVolume();
			List<BigDecimal> adjCloses = adjCloseMap.getAdjclose();
			for (int i = 0; i < tradedDatesInObject.size(); i++) {
				history.add(HistoricalDataNSE2021To2025.builder()
						.symbol(intermediateJsonLevel1.getMeta().getSymbol().substring(0,
								intermediateJsonLevel1.getMeta().getSymbol().length() - 3))
						.tradedDate(epochSecondToLocalDate(tradedDatesInObject.get(i).longValue())).open(opens.get(i))
						.close(closes.get(i)).low(lows.get(i)).high(highs.get(i)).adjustedClose(adjCloses.get(i))
						.volume(volumes.get(i)).createdDateTime(LocalDateTime.now()).build());
			}
		}
		return history;
	}

}
