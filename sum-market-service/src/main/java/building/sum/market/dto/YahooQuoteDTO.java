package building.sum.market.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooQuoteDTO {

	private String language;
	private String region;
	private String quoteType;
	private String typeDisp;
	private String quoteSourceName;
	private Boolean triggerable;
	private String customPriceAlertConfidence;
	private String currency;
	private String marketState;
	private String exchangeTimezoneName;
	private String exchangeTimezoneShortName;
	private BigDecimal gmtOffSetMilliseconds;
	private String market;
	private Boolean esgPopulated;
	private BigDecimal regularMarketChangePercent;
	private BigDecimal regularMarketPrice;
	private String exchange;
	private String shortName;
	private String longName;
	private String messageBoardId;
	private BigDecimal firstTradeDateMilliseconds;
	private BigDecimal priceHint;
	private BigDecimal regularMarketChange;
	private BigDecimal regularMarketTime;
	private BigDecimal regularMarketDayHigh;
	private String regularMarketDayRange;
	private BigDecimal regularMarketDayLow;
	private BigDecimal regularMarketVolume;
	private Boolean tradeable;
	private Boolean cryptoTradeable;
	private BigDecimal regularMarketPreviousClose;
	private BigDecimal bid;
	private BigDecimal ask;
	private BigDecimal bidSize;
	private BigDecimal askSize;
	private String prevName;
	private String nameChangeDate;
	private String underlyingSymbol;
	private String fullExchangeName;
	private String financialCurrency;
	private BigDecimal regularMarketOpen;
	private BigDecimal averageDailyVolume3Month;
	private BigDecimal averageDailyVolume10Day;
	private BigDecimal fiftyTwoWeekLowChange;
	private BigDecimal fiftyTwoWeekLowChangePercent;
	private String fiftyTwoWeekRange;
	private BigDecimal fiftyTwoWeekHighChange;
	private BigDecimal fiftyTwoWeekHighChangePercent;
	private BigDecimal fiftyTwoWeekLow;
	private BigDecimal fiftyTwoWeekHigh;
	private BigDecimal earningsTimestamp;
	private BigDecimal earningsTimestampStart;
	private BigDecimal earningsTimestampEnd;
	private BigDecimal trailingAnnualDividendRate;
	private BigDecimal trailingPE;
	private BigDecimal dividendRate;
	private BigDecimal trailingAnnualDividendYield;
	private BigDecimal dividendYield;
	private BigDecimal epsTrailingTwelveMonths;
	private BigDecimal epsForward;
	private BigDecimal epsCurrentYear;
	private BigDecimal priceEpsCurrentYear;
	private BigDecimal sharesOutstanding;
	private BigDecimal bookValue;
	private BigDecimal fiftyDayAverage;
	private BigDecimal fiftyDayAverageChange;
	private BigDecimal fiftyDayAverageChangePercent;
	private BigDecimal twoHundredDayAverage;
	private BigDecimal twoHundredDayAverageChange;
	private BigDecimal twoHundredDayAverageChangePercent;
	private BigDecimal marketCap;
	private BigDecimal forwardPE;
	private BigDecimal priceToBook;
	private BigDecimal sourceInterval;
	private BigDecimal exchangeDataDelayedBy;
	private BigDecimal ytdReturn;
	private BigDecimal trailingThreeMonthReturns;
	private String averageAnalystRating;
	private String symbol;
	private BigDecimal fiftyTwoWeekChangePercent;

}
