package building.sum.market.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import building.sum.market.dto.YahooFinanceHistoricalResponseDTO;
import building.sum.market.dto.YahooQuoteDTO;
import building.sum.market.model.InitiationMode;
import building.sum.market.model.Market;
import building.sum.market.service.MarketService;
import building.sum.market.utility.SumUtility;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/market")
public class MarketController {

	private static final Logger log = LogManager.getLogger();

	private static final DateTimeFormatter DMY_FORMATTER = SumUtility.DMY_FORMATTER;

	private final MarketService marketService;

	@GetMapping("/last-day-quote")
	public ResponseEntity<YahooQuoteDTO> getStockQuote(@RequestParam(required = false) String market,
			@RequestParam String symbol) {
		Market marketEnum = setDefaultMarketIfNotPresent(market);
		log.info("Request received to fetch stock - {} data from market - {}", symbol, market);
		return new ResponseEntity<>(marketService.getQuote(marketEnum, symbol.toUpperCase()), HttpStatus.OK);
	}

	@GetMapping("/historical-quote")
	public ResponseEntity<YahooFinanceHistoricalResponseDTO> getHistoricalStockQuote(
			@RequestParam(required = false) String market, @RequestParam String symbol, @RequestParam String from,
			@RequestParam String to) {
		Market marketEnum = setDefaultMarketIfNotPresent(market);
		log.info("Request received to fetch historical stock data for - {} data from market - {} between {} - {}",
				symbol, market, from, to);
		return new ResponseEntity<>(marketService.getHistoricalStockQuote(marketEnum, symbol.toUpperCase(), from, to),
				HttpStatus.OK);
	}

	@GetMapping("/historical-quote-save")
	@ResponseStatus(code = HttpStatus.OK)
	public void saveHistoricalStocksQuotes(@RequestParam(required = false) String to,
			@RequestParam(required = false) InitiationMode mode) {
		String newTo = setDefaultToDateIfNotPresent(to);
		log.info("Request received to save historical stock data between last saved date and - {} in - {}", newTo,
				mode);
		marketService.saveHistoricalStockQuote(newTo, mode);
	}

	@GetMapping("/historical-quote-save-updated-date")
	@ResponseStatus(code = HttpStatus.OK)
	public void saveHistoricalStocksUpdatedDate(@RequestParam(required = false) InitiationMode mode) {
		log.info("Request received to save/update historical stock updated dates in - {}", mode);
		marketService.updateStockUpdatedDates(mode);
	}

	private Market setDefaultMarketIfNotPresent(String market) {
		if (market == null) {
			return Market.NSE;
		} else {
			return Market.valueOf(market.toUpperCase());
		}
	}

	private String setDefaultToDateIfNotPresent(String to) {
		if (to == null) {
			return LocalDate.now().format(DMY_FORMATTER);
		} else {
			return to;
		}
	}

}
