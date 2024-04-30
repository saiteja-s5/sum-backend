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

	@GetMapping("/{userJoinKey}/{fundId}")
	public ResponseEntity<FundDTO> getFund(@PathVariable String userJoinKey, @PathVariable Long fundId) {
		log.info("Request received to get fund with Id - {} for user - {}", fundId, userJoinKey);
		return new ResponseEntity<>(fundService.getFund(userJoinKey, fundId), HttpStatus.OK);
	}

	@GetMapping("/{userJoinKey}")
	public ResponseEntity<List<FundDTO>> getFunds(@PathVariable String userJoinKey) {
		log.info("Request received to get all funds for user - {}", userJoinKey);
		return new ResponseEntity<>(fundService.getFunds(userJoinKey), HttpStatus.OK);
	}

	@DeleteMapping("/{userJoinKey}/{fundId}")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteFund(@PathVariable String userJoinKey, @PathVariable Long fundId) {
		log.info("Request received to delete fund with Id - {} for user - {}", fundId, userJoinKey);
		fundService.deleteFund(userJoinKey, fundId);
	}

}
