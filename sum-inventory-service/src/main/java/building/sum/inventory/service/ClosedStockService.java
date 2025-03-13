package building.sum.inventory.service;

import java.util.List;

import building.sum.inventory.dto.ClosedStockDTO;
import building.sum.inventory.model.ClosedStock;

public interface ClosedStockService {

	void postClosedStock(ClosedStock closedStock);

	ClosedStockDTO getClosedStock(String userJoinKey, Long closedStockId);

	List<ClosedStockDTO> getClosedStocks(String userJoinKey);

	void deleteClosedStock(String userJoinKey, Long closedStockId);

	void deleteClosedStocks(String userJoinKey);

}
