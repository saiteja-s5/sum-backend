package building.sum.market.utility;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class SumUtility {

	public static final DateTimeFormatter DMY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public static final Integer IS_ACTIVE = 1;

	public static final LocalDateTime HISTORICAL_DATE_START_2021 = LocalDateTime.of(2021, 1, 1, 0, 0);

	public static final int MONTHS_IN_YEAR = 12;

	public static final Double BROKER_MISC_CHARGES = 10.0;

	public static final String LAST_UPDATED_TABLE_PK = "LAST_UPDATED_MASTER_RECORD_KEY";

	private SumUtility() {
	}

	public static BigDecimal roundTo(Double number, int places) {
		return BigDecimal.valueOf(Math.round(number * Math.pow(10, places)) / Math.pow(10, places));
	}

	public static BigDecimal getPercentTarget(Double percent, Period holdDuration, int quantity, Double buyPrice) {
		int months = holdDuration.getYears() * MONTHS_IN_YEAR + holdDuration.getMonths()
				+ (holdDuration.getDays() > 0 ? 1 : 0);
		return roundTo((months * percent * buyPrice * 0.01) + buyPrice + (BROKER_MISC_CHARGES / quantity), 2);
	}

	public static BigDecimal getPercentageReturn(Double buyPrice, Double sellPrice) {
		return roundTo(((sellPrice - buyPrice) / buyPrice) * 100, 2);
	}

}
