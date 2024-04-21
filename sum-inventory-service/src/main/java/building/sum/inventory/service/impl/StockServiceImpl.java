package building.sum.inventory.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.inventory.dto.StockDTO;
import building.sum.inventory.exception.ResourceNotDeletedException;
import building.sum.inventory.exception.ResourceNotFoundException;
import building.sum.inventory.exception.ResourceNotPostedException;
import building.sum.inventory.model.Stock;
import building.sum.inventory.repository.StockRepository;
import building.sum.inventory.service.StockService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StockServiceImpl implements StockService {

	private static final Logger log = LogManager.getLogger();

	private final StockRepository stockRepository;

	@Override
	public void postStock(Stock stock) {
		try {
			stockRepository.save(stock);
		} catch (Exception e) {
			log.error("Stock - {} not posted", stock.getStockSymbol());
			throw new ResourceNotPostedException(e.getMessage());
		}
	}

	@Override
	public StockDTO getStock(Long stockId) {
		try {
			Optional<Stock> stockContainer = stockRepository.findById(stockId);
			if (stockContainer.isPresent()) {
				Stock stock = stockContainer.get();
				return stock2DTO(stock);
			} else {
				throw new ResourceNotFoundException(String.format("Stock with Id - %d not found"));
			}
		} catch (Exception e) {
			log.error("Stock with Id - {} not found", stockId);
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<StockDTO> getStocks() {
		try {
			List<Stock> savedStocks = stockRepository.findAll();
			if (savedStocks.isEmpty()) {
				log.warn("No stocks found");
				return new ArrayList<>();
			}
			return savedStocks.stream().map(this::stock2DTO).toList();
		} catch (Exception e) {
			log.error("Unable to fetch stocks");
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public void deleteStock(Long stockId) {
		try {
			Optional<Stock> savedStock = stockRepository.findById(stockId);
			if (savedStock.isPresent()) {
				stockRepository.deleteById(stockId);
			} else {
				log.warn("Requested stock with Id - {} not found", stockId);
				throw new ResourceNotFoundException(String.format("Stock with Id - %d not found"));
			}
		} catch (Exception e) {
			log.error("Stock with Id - {} not deleted", stockId);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	private StockDTO stock2DTO(Stock stock) {
		return StockDTO.builder().stockId(stock.getStockId()).stockName(stock.getStockName())
				.stockSymbol(stock.getStockSymbol()).investmentDate(stock.getInvestmentDate())
				.quantity(stock.getQuantity()).buyPrice(stock.getBuyPrice()).build();
	}

}
