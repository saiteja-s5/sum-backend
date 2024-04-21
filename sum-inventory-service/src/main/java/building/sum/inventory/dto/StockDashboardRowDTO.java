package building.sum.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;

import building.sum.inventory.model.Stock;
import building.sum.inventory.utility.SumUtility;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDashboardRowDTO {

	@Autowired
	private SumUtility utility;

	private String stockName;
	private Integer quantity;
	private LocalDate buyDate;
	private BigDecimal buyPrice;
	private BigDecimal buyValue;
	private Period holdDuration;
	private BigDecimal onePercentTarget;
	private BigDecimal twoPercentTarget;

	public StockDashboardRowDTO(String stockName, Integer quantity, LocalDate buyDate, BigDecimal buyPrice) {
		super();
		this.stockName = stockName;
		this.quantity = quantity;
		this.buyDate = buyDate;
		this.buyPrice = buyPrice;
		this.buyValue = buyValue();
		this.holdDuration = holdDuration();
		this.onePercentTarget = onePercentTarget();
		this.twoPercentTarget = twoPercentTarget();
	}

	public StockDashboardRowDTO(Stock stock) {
		this(stock.getStockName(), stock.getQuantity(), stock.getInvestmentDate(), stock.getBuyPrice());
	}

	private BigDecimal buyValue() {
		return BigDecimal.valueOf(buyPrice.doubleValue() * quantity);
	}

	private Period holdDuration() {
		return Period.between(buyDate, LocalDate.now());
	}

	private BigDecimal onePercentTarget() {
		return utility.roundTo(
				utility.getPercentTarget(1.0, holdDuration(), quantity, buyPrice.doubleValue()).doubleValue(), 2);
	}

	private BigDecimal twoPercentTarget() {
		return utility.roundTo(
				utility.getPercentTarget(2.0, holdDuration(), quantity, buyPrice.doubleValue()).doubleValue(), 2);
	}

	@Override
	public String toString() {
		return stockName + "," + quantity + "," + buyDate + "," + buyPrice + "," + buyValue + "," + holdDuration + ","
				+ onePercentTarget + "," + twoPercentTarget;
	}

}
