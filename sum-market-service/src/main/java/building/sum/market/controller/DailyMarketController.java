package building.sum.market.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import building.sum.market.dto.YahooQuoteDTO;
import building.sum.market.model.Market;
import building.sum.market.service.DailyMarketService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/daily-market")
public class DailyMarketController {

	private static final Logger log = LogManager.getLogger();

	private final DailyMarketService dailyMarketService;

	@GetMapping("/quote")
	public ResponseEntity<YahooQuoteDTO> getStockQuote(@RequestParam Optional<Market> market,
			@RequestParam String symbol) {
		Market tempMarket = market.isPresent() ? market.get() : Market.NSE;
		log.info("Request received to fetch stock - {} data from market - {}", symbol, tempMarket);
		return new ResponseEntity<>(dailyMarketService.getQuote(tempMarket, symbol), HttpStatus.OK);
	}

}
