package building.sum.inventory.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.inventory.model.TableLastUpdateDetails;
import building.sum.inventory.repository.TableLastUpdateDetailsRepository;
import building.sum.inventory.service.CommonService;
import building.sum.inventory.utility.SumUtility;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommonServiceImpl implements CommonService {

	private static final Logger log = LogManager.getLogger();

	private static final String TABLE_UPDATE_PK = SumUtility.LAST_UPDATED_TABLE_PK;

	private static final DateTimeFormatter YMD_FORMATTER = SumUtility.YMD_FORMATTER;

	private final TableLastUpdateDetailsRepository tableLastUpdateDetailsRepository;

	@Override
	public TableLastUpdateDetails putTableLastUpdateDetails(String column, String value) {
		log.debug(">>>>> putTableLastUpdateDetails args - {}, {}", column, value);
		Optional<TableLastUpdateDetails> container = tableLastUpdateDetailsRepository.findById(TABLE_UPDATE_PK);
		if (container.isPresent()) {
			TableLastUpdateDetails old = container.get();
			switch (column) {
			case "openStockHoldingsUpdatedDateTime":
				old.setOpenStockHoldingsUpdatedDateTime(LocalDate.parse(value, YMD_FORMATTER).atStartOfDay());
				break;
			case "fundUpdatedDateTime":
				old.setFundUpdatedDateTime(LocalDate.parse(value, YMD_FORMATTER).atStartOfDay());
				break;
			case "dividendUpdatedDateTime":
				old.setDividendUpdatedDateTime(LocalDate.parse(value, YMD_FORMATTER).atStartOfDay());
				break;
			case "closedStockHoldingsUpdatedDateTime":
				old.setClosedStockHoldingsUpdatedDateTime(LocalDate.parse(value, YMD_FORMATTER).atStartOfDay());
				break;
			default:
				break;
			}
			old.setLastUpdatedColumn(column);
			old.setLastUpdatedDateTime(LocalDateTime.now());
			log.debug(">>>>> putTableLastUpdateDetails args - {}, {}", column, value);
			return tableLastUpdateDetailsRepository.save(old);
		} else {
			return TableLastUpdateDetails.builder().build();
		}

	}

}
