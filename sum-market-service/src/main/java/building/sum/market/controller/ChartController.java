package building.sum.market.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import building.sum.market.dto.FundChartDTO;
import building.sum.market.dto.PortfolioChartDTO;
import building.sum.market.dto.QuoteChartDTO;
import building.sum.market.model.Market;
import building.sum.market.service.ChartService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/chart")
public class ChartController {

	private static final Logger log = LogManager.getLogger();

	private final ChartService chartService;

	@GetMapping("/quote-history")
	public ResponseEntity<QuoteChartDTO> getQuoteChart(@RequestParam(required = false) String market,
			@RequestParam String symbol) {
		Market marketEnum = setDefaultMarketIfNotPresent(market);
		log.info("Request received to fetch chart for quote - {} data from market - {}", symbol, market);
		return new ResponseEntity<>(chartService.getQuoteChart(marketEnum, symbol.toUpperCase()), HttpStatus.OK);
	}

	@GetMapping("/portfolio/{userJoinKey}")
	public ResponseEntity<PortfolioChartDTO> getPortfolioChart(@PathVariable String userJoinKey) {
		log.info("Request received to fetch portfolio chart for user - {}", userJoinKey);
		return new ResponseEntity<>(chartService.getPortfolioChart(userJoinKey), HttpStatus.OK);
	}
	
	@GetMapping("/fund/{userJoinKey}")
	public ResponseEntity<FundChartDTO> getFundChart(@PathVariable String userJoinKey) {
		log.info("Request received to fetch fund chart for user - {}", userJoinKey);
		return new ResponseEntity<>(chartService.getFundChart(userJoinKey), HttpStatus.OK);
	}

	private Market setDefaultMarketIfNotPresent(String market) {
		if (market == null) {
			return Market.NSE;
		} else {
			return Market.valueOf(market.toUpperCase());
		}
	}

}
