package building.sum.inventory.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import building.sum.inventory.dto.DividendDTO;
import building.sum.inventory.dto.DividendDashboardDTO;
import building.sum.inventory.model.Dividend;
import building.sum.inventory.service.DividendService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/dividends")
public class DividendController {

	private static final Logger log = LogManager.getLogger();

	private final DividendService dividendService;

	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public void postDividend(@RequestBody @Valid Dividend dividend) {
		log.info("Request received to post dividend - {}", dividend.getCompanyName());
		dividendService.postDividend(dividend);
	}

	@GetMapping("/{dividendId}")
	public ResponseEntity<DividendDTO> getDividend(@PathVariable Long dividendId) {
		log.info("Request received to get dividend with Id - {}", dividendId);
		return new ResponseEntity<>(dividendService.getDividend(dividendId), HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<List<DividendDTO>> getDividends() {
		log.info("Request received to get all dividends");
		return new ResponseEntity<>(dividendService.getDividends(), HttpStatus.OK);
	}

	@DeleteMapping()
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteDividend(@PathVariable Long dividendId) {
		log.info("Request received to delete dividend with Id - {}", dividendId);
		dividendService.deleteDividend(dividendId);
	}

	@GetMapping("/dashboard")
	public ResponseEntity<DividendDashboardDTO> getCurrentEarnings() {
		log.info("Request received to get current dividend earnings");
		return new ResponseEntity<>(dividendService.getCurrentEarnings(), HttpStatus.OK);
	}

}
