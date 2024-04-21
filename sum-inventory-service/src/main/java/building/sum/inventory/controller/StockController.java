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

import building.sum.inventory.dto.StockDTO;
import building.sum.inventory.model.Stock;
import building.sum.inventory.service.StockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/stocks")
public class StockController {

	private static final Logger log = LogManager.getLogger();

	private final StockService stockService;

	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public void postStock(@RequestBody @Valid Stock stock) {
		log.info("Request received to post stock - {}", stock.getStockSymbol());
		stockService.postStock(stock);
	}

	@GetMapping("/{stockId}")
	public ResponseEntity<StockDTO> getStock(@PathVariable Long stockId) {
		log.info("Request received to get stock with Id - {}", stockId);
		return new ResponseEntity<>(stockService.getStock(stockId), HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<List<StockDTO>> getStocks() {
		log.info("Request received to get all stocks");
		return new ResponseEntity<>(stockService.getStocks(), HttpStatus.OK);
	}

	@DeleteMapping()
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteStock(@PathVariable Long stockId) {
		log.info("Request received to delete stock with Id - {}", stockId);
		stockService.deleteStock(stockId);
	}

}
