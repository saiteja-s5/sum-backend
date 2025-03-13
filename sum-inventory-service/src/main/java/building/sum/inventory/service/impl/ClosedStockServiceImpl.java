package building.sum.inventory.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.inventory.dto.ClosedStockDTO;
import building.sum.inventory.exception.ResourceNotDeletedException;
import building.sum.inventory.exception.ResourceNotFoundException;
import building.sum.inventory.exception.ResourceNotPostedException;
import building.sum.inventory.model.ClosedStock;
import building.sum.inventory.repository.ClosedStockRepository;
import building.sum.inventory.service.ClosedStockService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClosedStockServiceImpl implements ClosedStockService {

	private static final Logger log = LogManager.getLogger();

	private final ClosedStockRepository closedStockRepository;

	@Override
	public void postClosedStock(ClosedStock closedStock) {
		try {
			closedStockRepository.save(closedStock);
		} catch (Exception e) {
			log.error("Closed Stock - {} not posted", closedStock.getStockSymbol());
			throw new ResourceNotPostedException(e.getMessage());
		}
	}

	@Override
	public ClosedStockDTO getClosedStock(String userJoinKey, Long closedStockId) {
		try {
			Optional<ClosedStock> closedStockContainer = closedStockRepository
					.findByUserJoinKeyAndClosedStockId(userJoinKey, closedStockId);
			if (closedStockContainer.isPresent()) {
				ClosedStock closedStock = closedStockContainer.get();
				return closedStock2DTO(closedStock);
			} else {
				throw new ResourceNotFoundException(
						String.format("Closed Stock with Id - %d not found", closedStockId));
			}
		} catch (Exception e) {
			log.error("Closed Stock with Id - {} not found", closedStockId);
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<ClosedStockDTO> getClosedStocks(String userJoinKey) {
		try {
			List<ClosedStock> savedClosedStocks = closedStockRepository.findAllByUserJoinKey(userJoinKey);
			if (savedClosedStocks.isEmpty()) {
				log.warn("No closed stocks found");
				return new ArrayList<>();
			}
			return savedClosedStocks.stream().map(this::closedStock2DTO).toList();
		} catch (Exception e) {
			log.error("Unable to fetch closed stocks");
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public void deleteClosedStock(String userJoinKey, Long closedStockId) {
		try {
			Optional<ClosedStock> savedClosedStock = closedStockRepository
					.findByUserJoinKeyAndClosedStockId(userJoinKey, closedStockId);
			if (savedClosedStock.isPresent()) {
				closedStockRepository.deleteByUserJoinKeyAndClosedStockId(userJoinKey, closedStockId);
			} else {
				log.warn("Requested closed stock with Id - {} not found", closedStockId);
				throw new ResourceNotFoundException(
						String.format("Closed Stock with Id - %d not found", closedStockId));
			}
		} catch (Exception e) {
			log.error("Closed Stock with Id - {} not deleted", closedStockId);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	@Override
	public void deleteClosedStocks(String userJoinKey) {
		try {
			List<ClosedStock> savedClosedStocks = closedStockRepository.findAllByUserJoinKey(userJoinKey);
			if (!savedClosedStocks.isEmpty()) {
				closedStockRepository.deleteByUserJoinKey(userJoinKey);
			} else {
				log.warn("Requested closed stocks for user - {} not found", userJoinKey);
				throw new ResourceNotFoundException(
						String.format("Closed Stocks for user - %s not found", userJoinKey));
			}
		} catch (Exception e) {
			log.error("Closed Stocks for user - {} not deleted", userJoinKey);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	private ClosedStockDTO closedStock2DTO(ClosedStock closedStock) {
		return ClosedStockDTO.builder().closedStockId(closedStock.getClosedStockId())
				.stockName(closedStock.getStockName()).boughtMarket(closedStock.getBoughtMarket())
				.stockSymbol(closedStock.getStockSymbol()).buyDate(closedStock.getBuyDate())
				.buyQuantity(closedStock.getBuyQuantity()).buyPrice(closedStock.getBuyPrice())
				.sellDate(closedStock.getSellDate()).sellQuantity(closedStock.getSellQuantity())
				.sellPrice(closedStock.getSellPrice()).build();
	}

}
