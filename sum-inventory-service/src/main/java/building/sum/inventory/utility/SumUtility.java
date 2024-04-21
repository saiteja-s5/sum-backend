package building.sum.inventory.utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

@Component
public class SumUtility {

	public static final int MONTHS_IN_YEAR = 12;

	public static final Double BROKER_MISC_CHARGES = 10.0;

	public static final LocalDate STOCK_START_DATE = LocalDate.of(2021, 1, 18);

	public static final LocalDate MUTUAL_FUND_START_DATE = LocalDate.of(2021, 8, 14);

	public static final LocalDate FUND_START_DATE = LocalDate.of(2021, 1, 18);

	public static final LocalDate DIVIDEND_START_DATE = LocalDate.of(2021, 3, 10);

	public static final LocalDate SOLD_START_DATE = LocalDate.of(2021, 1, 18);

	public static final LocalDate EPOCH_SECOND_EXCEEDS_INTEGER_ON = LocalDate.of(2038, 1, 19);

	public BigDecimal getPercentTarget(Double percent, Period holdDuration, int quantity, Double buyPrice) {
		int months = holdDuration.getYears() * MONTHS_IN_YEAR + holdDuration.getMonths()
				+ (holdDuration.getDays() > 0 ? 1 : 0);
		return roundTo((months * percent * buyPrice * 0.01) + buyPrice + (BROKER_MISC_CHARGES / quantity), 2);
	}

	public BigDecimal getPercentageReturn(Double buyPrice, Double sellPrice) {
		return roundTo(((sellPrice - buyPrice) / buyPrice) * 100, 2);
	}

	public BigDecimal roundTo(Double number, int places) {
		return BigDecimal.valueOf(Math.round(number * Math.pow(10, places)) / Math.pow(10, places));
	}

}
