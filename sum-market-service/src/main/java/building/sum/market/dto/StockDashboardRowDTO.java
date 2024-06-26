package building.sum.market.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import building.sum.market.model.Market;
import building.sum.market.model.Stock;
import building.sum.market.utility.SumUtility;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDashboardRowDTO {

	private String stockName;
	private String stockSymbol;
	private Market market;
	private Integer quantity;
	private LocalDateTime buyDate;
	private BigDecimal buyPrice;
	private BigDecimal buyValue;
	private Period holdDuration;
	private BigDecimal onePercentTarget;
	private BigDecimal twoPercentTarget;

	public StockDashboardRowDTO(String stockName, String stockSymbol, Market market, Integer quantity,
			LocalDateTime buyDate, BigDecimal buyPrice) {
		super();
		this.stockName = stockName;
		this.stockSymbol = stockSymbol;
		this.market = market;
		this.quantity = quantity;
		this.buyDate = buyDate;
		this.buyPrice = buyPrice;
		this.buyValue = buyValue();
		this.holdDuration = holdDuration();
		this.onePercentTarget = onePercentTarget();
		this.twoPercentTarget = twoPercentTarget();
	}

	public StockDashboardRowDTO(Stock stock) {
		this(stock.getStockName(), stock.getStockSymbol(), stock.getBoughtMarket(), stock.getQuantity(),
				stock.getInvestmentDate(), stock.getBuyPrice());
	}

	private BigDecimal buyValue() {
		return BigDecimal.valueOf(buyPrice.doubleValue() * quantity);
	}

	private Period holdDuration() {
		return Period.between(buyDate.toLocalDate(), LocalDate.now());
	}

	private BigDecimal onePercentTarget() {
		return SumUtility.roundTo(
				SumUtility.getPercentTarget(1.0, this.holdDuration, quantity, buyPrice.doubleValue()).doubleValue(), 2);
	}

	private BigDecimal twoPercentTarget() {
		return SumUtility.roundTo(
				SumUtility.getPercentTarget(2.0, this.holdDuration, quantity, buyPrice.doubleValue()).doubleValue(), 2);
	}

	@Override
	public String toString() {
		return stockName + "," + stockSymbol + "," + market + "," + quantity + "," + buyDate + "," + buyPrice + ","
				+ buyValue + "," + holdDuration + "," + onePercentTarget + "," + twoPercentTarget;
	}

}
