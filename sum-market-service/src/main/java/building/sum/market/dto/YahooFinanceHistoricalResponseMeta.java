package building.sum.market.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YahooFinanceHistoricalResponseMeta {

	private String currency;
	private String symbol;
	private String exchangeName;
	private String fullExchangeName;
	private String instrumentType;
	private Long firstTradeDate;
	private Long regularMarketTime;
	private Boolean hasPrePostMarketData;
	private Integer gmtoffset;
	private String timezone;
	private String exchangeTimezoneName;
	private Double regularMarketPrice;
	private Double fiftyTwoWeekHigh;
	private Double fiftyTwoWeekLow;
	private Double regularMarketDayHigh;
	private Double regularMarketDayLow;
	private Long regularMarketVolume;
	private String longName;
	private String shortName;
	private Double chartPreviousClose;
	private Integer priceHint;
	private YahooFinanceHistoricalResponseCurrentTradingPeriod currentTradingPeriod;
	private String dataGranularity;
	private String range;
	private List<String> validRanges;

}
