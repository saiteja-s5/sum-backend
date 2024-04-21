package building.sum.inventory.service;

import java.util.List;

import building.sum.inventory.dto.StockDTO;
import building.sum.inventory.model.Stock;

public interface StockService {

	void postStock(Stock stock);

	StockDTO getStock(Long stockId);

	List<StockDTO> getStocks();

	void deleteStock(Long stockId);

}
