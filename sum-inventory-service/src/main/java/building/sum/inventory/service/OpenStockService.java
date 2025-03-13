package building.sum.inventory.service;

import java.util.List;

import building.sum.inventory.dto.OpenStockDTO;
import building.sum.inventory.model.OpenStock;

public interface OpenStockService {

	void postOpenStock(OpenStock openStock);

	OpenStockDTO getOpenStock(String userJoinKey, Long openStockId);

	List<OpenStockDTO> getOpenStocks(String userJoinKey);

	void deleteOpenStock(String userJoinKey, Long openStockId);

	void deleteOpenStocks(String userJoinKey);

}
