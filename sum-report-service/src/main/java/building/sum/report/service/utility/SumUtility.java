package building.sum.report.service.utility;

import java.math.BigDecimal;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class SumUtility {

	public static final DateTimeFormatter DMYW_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy, EEEE");

	public static final DateTimeFormatter DMY_WITH_TIME = DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm a");

	public static final String APP_DETAILS_TABLE_PK = "APP_DETAILS_MASTER_RECORD_KEY";

	public static final int MONTHS_IN_YEAR = 12;

	public static final Double BROKER_MISC_CHARGES = 10.0;

	private SumUtility() {
	}

	public static BigDecimal getPercentTarget(Double percent, Period holdDuration, int quantity, Double buyPrice) {
		int months = holdDuration.getYears() * MONTHS_IN_YEAR + holdDuration.getMonths()
				+ (holdDuration.getDays() > 0 ? 1 : 0);
		return roundTo((months * percent * buyPrice * 0.01) + buyPrice + (BROKER_MISC_CHARGES / quantity), 2);
	}

	public static BigDecimal roundTo(Double number, int places) {
		return BigDecimal.valueOf(Math.round(number * Math.pow(10, places)) / Math.pow(10, places));
	}

}
