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

import building.sum.market.dto.StockDashboardDTO;
import building.sum.market.service.DashboardService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/daily-market")
public class DailyMarketController {

	private static final Logger log = LogManager.getLogger();

	private final DashboardService dashboardService;

	@GetMapping("/{userJoinKey}/dashboard")
	public ResponseEntity<StockDashboardDTO> getCurrentHoldings(@PathVariable String userJoinKey) {
		log.info("Request received to get current holdings dashboard for user - {}", userJoinKey);
		return new ResponseEntity<>(dashboardService.getCurrentHoldings(userJoinKey), HttpStatus.OK);
	}

}
