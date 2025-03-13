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

import building.sum.inventory.dto.ClosedStockDTO;
import building.sum.inventory.model.ClosedStock;
import building.sum.inventory.service.ClosedStockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/closed-stocks")
public class ClosedStockController {

	private static final Logger log = LogManager.getLogger();

	private final ClosedStockService closedStockService;

	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public void postClosedStock(@RequestBody @Valid ClosedStock closedStock) {
		log.info("Request received to post closed stock - {}", closedStock.getStockSymbol());
		closedStockService.postClosedStock(closedStock);
	}

	@GetMapping("/{userJoinKey}/{closedStockId}")
	public ResponseEntity<ClosedStockDTO> getClosedStock(@PathVariable String userJoinKey, @PathVariable Long closedStockId) {
		log.info("Request received to get closed stock with Id - {} for user - {}", closedStockId, userJoinKey);
		return new ResponseEntity<>(closedStockService.getClosedStock(userJoinKey, closedStockId), HttpStatus.OK);
	}

	@GetMapping("/{userJoinKey}")
	public ResponseEntity<List<ClosedStockDTO>> getClosedStocks(@PathVariable String userJoinKey) {
		log.info("Request received to get all closed stocks for user - {}", userJoinKey);
		return new ResponseEntity<>(closedStockService.getClosedStocks(userJoinKey), HttpStatus.OK);
	}

	@DeleteMapping("/{userJoinKey}/{closedStockId}")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteClosedStock(@PathVariable String userJoinKey, @PathVariable Long closedStockId) {
		log.info("Request received to delete closed stock with Id - {} for user - {}", closedStockId, userJoinKey);
		closedStockService.deleteClosedStock(userJoinKey, closedStockId);
	}

	@DeleteMapping("/{userJoinKey}")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteClosedStocks(@PathVariable String userJoinKey) {
		log.info("Request received to delete closed stocks for user - {}", userJoinKey);
		closedStockService.deleteClosedStocks(userJoinKey);
	}

}
