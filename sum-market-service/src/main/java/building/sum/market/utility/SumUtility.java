package building.sum.market.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SumUtility {

	public static final DateTimeFormatter DMY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public static final Integer IS_ACTIVE = 1;

	public static final LocalDateTime HISTORICAL_DATE_START_2021 = LocalDateTime.of(2021, 1, 1, 0, 0);

	private SumUtility() {
	}

}
