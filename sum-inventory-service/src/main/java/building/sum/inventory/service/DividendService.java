package building.sum.inventory.service;

import java.util.List;

import building.sum.inventory.dto.DividendDTO;
import building.sum.inventory.dto.DividendDashboardDTO;
import building.sum.inventory.model.Dividend;

public interface DividendService {

	void postDividend(Dividend dividend);

	DividendDTO getDividend(Long dividendId);

	List<DividendDTO> getDividends();

	void deleteDividend(Long dividendId);

	DividendDashboardDTO getCurrentEarnings();

}
