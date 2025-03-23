package building.sum.inventory.service;

import building.sum.inventory.model.TableLastUpdateDetails;

public interface CommonService {

	TableLastUpdateDetails putTableLastUpdateDetails(String column, String value);

}
