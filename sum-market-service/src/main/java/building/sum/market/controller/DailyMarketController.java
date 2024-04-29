package building.sum.market.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import building.sum.market.dto.StockDashboardDTO;
import building.sum.market.dto.YahooQuoteDTO;
import building.sum.market.model.Market;
import building.sum.market.service.DailyMarketService;
import building.sum.market.service.DashboardService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/daily-market")
public class DailyMarketController {

	private static final Logger log = LogManager.getLogger();

	private final DailyMarketService dailyMarketService;

	private final DashboardService dashboardService;

	@GetMapping("/quote")
	public ResponseEntity<YahooQuoteDTO> getStockQuote(@RequestParam(required = false) String market,
			@RequestParam String symbol) {
		Market marketEnum;
		if (market == null) {
			marketEnum = Market.NSE;
		} else {
			marketEnum = Market.valueOf(market.toUpperCase());
		}
		log.info("Request received to fetch stock - {} data from market - {}", symbol, market);
		return new ResponseEntity<>(dailyMarketService.getQuote(marketEnum, symbol.toUpperCase()), HttpStatus.OK);
	}

	@GetMapping("/dashboard")
	public ResponseEntity<StockDashboardDTO> getCurrentHoldings() {
		log.info("Request received to get current holdings");
		return new ResponseEntity<>(dashboardService.getCurrentHoldings(), HttpStatus.OK);
	}

}
