package building.sum.market.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import building.sum.market.dto.ClosedStockDashboardDTO;
import building.sum.market.dto.DividendDashboardDTO;
import building.sum.market.dto.FundDashboardDTO;
import building.sum.market.dto.OpenStockDashboardDTO;
import building.sum.market.service.DashboardService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

	private static final Logger log = LogManager.getLogger();

	private final DashboardService dashboardService;

	@GetMapping("/{userJoinKey}/open-stock")
	public ResponseEntity<OpenStockDashboardDTO> getOpenStockHoldings(@PathVariable String userJoinKey) {
		log.info("Request received to get open stock holdings dashboard for user - {}", userJoinKey);
		return new ResponseEntity<>(dashboardService.getOpenStockHoldings(userJoinKey), HttpStatus.OK);
	}

	@GetMapping("/{userJoinKey}/fund")
	public ResponseEntity<FundDashboardDTO> getFunds(@PathVariable String userJoinKey) {
		log.info("Request received to get funds dashboard for user - {}", userJoinKey);
		return new ResponseEntity<>(dashboardService.getFunds(userJoinKey), HttpStatus.OK);
	}

	@GetMapping("/{userJoinKey}/dividend")
	public ResponseEntity<DividendDashboardDTO> getDividends(@PathVariable String userJoinKey) {
		log.info("Request received to get dividends dashboard for user - {}", userJoinKey);
		return new ResponseEntity<>(dashboardService.getDividends(userJoinKey), HttpStatus.OK);
	}

	@GetMapping("/{userJoinKey}/closed-stock")
	public ResponseEntity<ClosedStockDashboardDTO> getClosedStockHoldings(@PathVariable String userJoinKey) {
		log.info("Request received to get closed stock holdings dashboard for user - {}", userJoinKey);
		return new ResponseEntity<>(dashboardService.getClosedStockHoldings(userJoinKey), HttpStatus.OK);
	}

}
