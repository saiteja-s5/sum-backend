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

import building.sum.inventory.dto.FundDTO;
import building.sum.inventory.dto.FundDashboardDTO;
import building.sum.inventory.model.Fund;
import building.sum.inventory.service.FundService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/funds")
public class FundController {

	private static final Logger log = LogManager.getLogger();

	private final FundService fundService;

	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public void postFund(@RequestBody @Valid Fund fund) {
		log.info("Request received to post fund - {}", fund.getCreditedAmount());
		fundService.postFund(fund);
	}

	@GetMapping("/{fundId}")
	public ResponseEntity<FundDTO> getFund(@PathVariable Long fundId) {
		log.info("Request received to get fund with Id - {}", fundId);
		return new ResponseEntity<>(fundService.getFund(fundId), HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<List<FundDTO>> getFunds() {
		log.info("Request received to get all funds");
		return new ResponseEntity<>(fundService.getFunds(), HttpStatus.OK);
	}

	@DeleteMapping()
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteFund(@PathVariable Long fundId) {
		log.info("Request received to delete fund with Id - {}", fundId);
		fundService.deleteFund(fundId);
	}

	@GetMapping("/dashboard")
	public ResponseEntity<FundDashboardDTO> getTillDateFunds() {
		log.info("Request received to get all funds till date");
		return new ResponseEntity<>(fundService.getTillDateFunds(), HttpStatus.OK);
	}

}
