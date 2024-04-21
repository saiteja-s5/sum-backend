package building.sum.inventory.service;

import java.util.List;

import building.sum.inventory.dto.FundDTO;
import building.sum.inventory.dto.FundDashboardDTO;
import building.sum.inventory.model.Fund;

public interface FundService {

	void postFund(Fund fund);

	FundDTO getFund(Long fundId);

	List<FundDTO> getFunds();

	void deleteFund(Long fundId);

	FundDashboardDTO getTillDateFunds();

}
