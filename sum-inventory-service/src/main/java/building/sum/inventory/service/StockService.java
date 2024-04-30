package building.sum.inventory.service;

import java.util.List;

import building.sum.inventory.dto.StockDTO;
import building.sum.inventory.model.Stock;

public interface StockService {

	void postStock(Stock stock);

	StockDTO getStock(String userJoinKey, Long stockId);

	List<StockDTO> getStocks(String userJoinKey);

	void deleteStock(String userJoinKey, Long stockId);

}
