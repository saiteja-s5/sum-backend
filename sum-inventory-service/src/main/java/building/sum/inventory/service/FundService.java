package building.sum.inventory.service;

import java.util.List;

import building.sum.inventory.dto.FundDTO;
import building.sum.inventory.model.Fund;

public interface FundService {

	void postFund(Fund fund);

	FundDTO getFund(String userJoinKey, Long fundId);

	List<FundDTO> getFunds(String userJoinKey);

	void deleteFund(String userJoinKey, Long fundId);

}
