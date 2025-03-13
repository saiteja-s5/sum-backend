package building.sum.inventory.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.inventory.dto.OpenStockDTO;
import building.sum.inventory.exception.ResourceNotDeletedException;
import building.sum.inventory.exception.ResourceNotFoundException;
import building.sum.inventory.exception.ResourceNotPostedException;
import building.sum.inventory.model.OpenStock;
import building.sum.inventory.repository.OpenStockRepository;
import building.sum.inventory.service.OpenStockService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OpenStockServiceImpl implements OpenStockService {

	private static final Logger log = LogManager.getLogger();

	private final OpenStockRepository openStockRepository;

	@Override
	public void postOpenStock(OpenStock openStock) {
		try {
			openStockRepository.save(openStock);
		} catch (Exception e) {
			log.error("Open Stock - {} not posted", openStock.getStockSymbol());
			throw new ResourceNotPostedException(e.getMessage());
		}
	}

	@Override
	public OpenStockDTO getOpenStock(String userJoinKey, Long openStockId) {
		try {
			Optional<OpenStock> openStockContainer = openStockRepository.findByUserJoinKeyAndOpenStockId(userJoinKey,
					openStockId);
			if (openStockContainer.isPresent()) {
				OpenStock openStock = openStockContainer.get();
				return openStock2DTO(openStock);
			} else {
				throw new ResourceNotFoundException(String.format("Open Stock with Id - %d not found", openStockId));
			}
		} catch (Exception e) {
			log.error("Open Stock with Id - {} not found", openStockId);
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<OpenStockDTO> getOpenStocks(String userJoinKey) {
		try {
			List<OpenStock> savedOpenStocks = openStockRepository.findAllByUserJoinKey(userJoinKey);
			if (savedOpenStocks.isEmpty()) {
				log.warn("No open stocks found");
				return new ArrayList<>();
			}
			return savedOpenStocks.stream().map(this::openStock2DTO).toList();
		} catch (Exception e) {
			log.error("Unable to fetch open stocks");
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public void deleteOpenStock(String userJoinKey, Long openStockId) {
		try {
			Optional<OpenStock> savedOpenStock = openStockRepository.findByUserJoinKeyAndOpenStockId(userJoinKey,
					openStockId);
			if (savedOpenStock.isPresent()) {
				openStockRepository.deleteByUserJoinKeyAndOpenStockId(userJoinKey, openStockId);
			} else {
				log.warn("Requested open stock with Id - {} not found", openStockId);
				throw new ResourceNotFoundException(String.format("Open Stock with Id - %d not found", openStockId));
			}
		} catch (Exception e) {
			log.error("Open Stock with Id - {} not deleted", openStockId);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	@Override
	public void deleteOpenStocks(String userJoinKey) {
		try {
			List<OpenStock> savedOpenStocks = openStockRepository.findAllByUserJoinKey(userJoinKey);
			if (!savedOpenStocks.isEmpty()) {
				openStockRepository.deleteByUserJoinKey(userJoinKey);
			} else {
				log.warn("Requested open stocks for user - {} not found", userJoinKey);
				throw new ResourceNotFoundException(String.format("Open Stocks for user - %s not found", userJoinKey));
			}
		} catch (Exception e) {
			log.error("Open Stocks for user - {} not deleted", userJoinKey);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	private OpenStockDTO openStock2DTO(OpenStock openStock) {
		return OpenStockDTO.builder().openStockId(openStock.getOpenStockId()).stockName(openStock.getStockName())
				.boughtMarket(openStock.getBoughtMarket()).stockSymbol(openStock.getStockSymbol())
				.investmentDate(openStock.getInvestmentDate()).quantity(openStock.getQuantity())
				.buyPrice(openStock.getBuyPrice()).build();
	}

}
