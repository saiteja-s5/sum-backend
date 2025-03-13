package building.sum.inventory.service;

import java.util.List;

import building.sum.inventory.dto.DividendDTO;
import building.sum.inventory.model.Dividend;

public interface DividendService {

	void postDividend(Dividend dividend);

	DividendDTO getDividend(String userJoinKey, Long dividendId);

	List<DividendDTO> getDividends(String userJoinKey);

	void deleteDividend(String userJoinKey, Long dividendId);

	void deleteDividends(String userJoinKey);
}
