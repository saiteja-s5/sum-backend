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

	@GetMapping("/{userJoinKey}/{dividendId}")
	public ResponseEntity<DividendDTO> getDividend(@PathVariable String userJoinKey, @PathVariable Long dividendId) {
		log.info("Request received to get dividend with Id - {} for user - {}", dividendId, userJoinKey);
		return new ResponseEntity<>(dividendService.getDividend(userJoinKey, dividendId), HttpStatus.OK);
	}

	@GetMapping("/{userJoinKey}")
	public ResponseEntity<List<DividendDTO>> getDividends(@PathVariable String userJoinKey) {
		log.info("Request received to get all dividends for user - {}", userJoinKey);
		return new ResponseEntity<>(dividendService.getDividends(userJoinKey), HttpStatus.OK);
	}

	@DeleteMapping("/{userJoinKey}/{dividendId}")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteDividend(@PathVariable String userJoinKey, @PathVariable Long dividendId) {
		log.info("Request received to delete dividend with Id - {} for user - {}", dividendId, userJoinKey);
		dividendService.deleteDividend(userJoinKey, dividendId);
	}

}
