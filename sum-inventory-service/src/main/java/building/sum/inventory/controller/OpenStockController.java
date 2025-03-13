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

import building.sum.inventory.dto.OpenStockDTO;
import building.sum.inventory.model.OpenStock;
import building.sum.inventory.service.OpenStockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/open-stocks")
public class OpenStockController {

	private static final Logger log = LogManager.getLogger();

	private final OpenStockService openStockService;

	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public void postOpenStock(@RequestBody @Valid OpenStock openStock) {
		log.info("Request received to post open stock - {}", openStock.getStockSymbol());
		openStockService.postOpenStock(openStock);
	}

	@GetMapping("/{userJoinKey}/{openStockId}")
	public ResponseEntity<OpenStockDTO> getOpenStock(@PathVariable String userJoinKey, @PathVariable Long openStockId) {
		log.info("Request received to get open stock with Id - {} for user - {}", openStockId, userJoinKey);
		return new ResponseEntity<>(openStockService.getOpenStock(userJoinKey, openStockId), HttpStatus.OK);
	}

	@GetMapping("/{userJoinKey}")
	public ResponseEntity<List<OpenStockDTO>> getOpenStocks(@PathVariable String userJoinKey) {
		log.info("Request received to get all open stocks for user - {}", userJoinKey);
		return new ResponseEntity<>(openStockService.getOpenStocks(userJoinKey), HttpStatus.OK);
	}

	@DeleteMapping("/{userJoinKey}/{openStockId}")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteOpenStock(@PathVariable String userJoinKey, @PathVariable Long openStockId) {
		log.info("Request received to delete open stock with Id - {} for user - {}", openStockId, userJoinKey);
		openStockService.deleteOpenStock(userJoinKey, openStockId);
	}

	@DeleteMapping("/{userJoinKey}")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteOpenStocks(@PathVariable String userJoinKey) {
		log.info("Request received to delete open stocks for user - {}", userJoinKey);
		openStockService.deleteOpenStocks(userJoinKey);
	}

}
