package building.sum.market.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Period;

import building.sum.market.model.ClosedStock;
import building.sum.market.model.Market;
import building.sum.market.utility.SumUtility;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClosedStockDashboardRowDTO {

	private String stockName;
	private Market market;
	private Integer buyQuantity;
	private LocalDateTime buyDate;
	private BigDecimal buyPrice;
	private BigDecimal buyValue;
	private Integer sellQuantity;
	private LocalDateTime sellDate;
	private BigDecimal sellPrice;
	private BigDecimal sellValue;
	private Period holdDuration;
	private BigDecimal profitLossValue;
	private BigDecimal profitLossPercent;
	private BigDecimal profitLossPercentPerMonth;

	public ClosedStockDashboardRowDTO(String stockName, Market market, Integer buyQuantity, LocalDateTime buyDate,
			BigDecimal buyPrice, Integer sellQuantity, LocalDateTime sellDate, BigDecimal sellPrice) {
		super();
		this.stockName = stockName;
		this.market = market;
		this.buyQuantity = buyQuantity;
		this.buyDate = buyDate;
		this.buyPrice = buyPrice;
		this.buyValue = buyValue();
		this.sellQuantity = sellQuantity;
		this.sellDate = sellDate;
		this.sellPrice = sellPrice;
		this.sellValue = sellValue();
		this.holdDuration = holdDuration();
		this.profitLossValue = profitLossValue();
		this.profitLossPercent = profitLossPercent();
		this.profitLossPercentPerMonth = profitLossPercentPerMonth();
	}

	public ClosedStockDashboardRowDTO(ClosedStock closedStock) {
		this(closedStock.getStockName(), closedStock.getBoughtMarket(), closedStock.getBuyQuantity(),
				closedStock.getBuyDate(), closedStock.getBuyPrice(), closedStock.getSellQuantity(),
				closedStock.getSellDate(), closedStock.getSellPrice());
	}

	private BigDecimal buyValue() {
		return BigDecimal.valueOf(buyPrice.doubleValue() * buyQuantity);
	}

	private BigDecimal sellValue() {
		return BigDecimal.valueOf(sellPrice.doubleValue() * sellQuantity);
	}

	private Period holdDuration() {
		return Period.between(buyDate.toLocalDate(), sellDate.toLocalDate());
	}

	private BigDecimal profitLossValue() {
		return SumUtility.roundTo(sellValue().subtract(buyValue()).doubleValue(), 2);
	}

	private BigDecimal profitLossPercent() {
		return SumUtility.roundTo(
				SumUtility.getPercentageReturn(buyValue().doubleValue(), sellValue().doubleValue()).doubleValue(), 2);
	}

	private BigDecimal profitLossPercentPerMonth() {
		int totalMonths = holdDuration().getYears() * 12 + holdDuration().getMonths();
		if (totalMonths == 0) {
			totalMonths = 1;
		}
		return SumUtility.roundTo(
				profitLossPercent().divide(BigDecimal.valueOf(totalMonths), RoundingMode.FLOOR).doubleValue(), 2);
	}

	@Override
	public String toString() {
		return stockName + "," + market + "," + buyQuantity + "," + buyDate + "," + buyPrice + "," + buyValue + ","
				+ sellQuantity + "," + sellDate + "," + sellPrice + "," + sellValue + "," + holdDuration + ","
				+ profitLossValue + "," + profitLossPercent + "," + profitLossPercentPerMonth;
	}

}
