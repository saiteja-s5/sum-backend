package building.sum.inventory.utility;

import java.time.format.DateTimeFormatter;

public class SumUtility {

	public static final String LAST_UPDATED_TABLE_PK = "LAST_UPDATED_MASTER_RECORD_KEY";

	public static final DateTimeFormatter YMD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private SumUtility() {
	}

}
