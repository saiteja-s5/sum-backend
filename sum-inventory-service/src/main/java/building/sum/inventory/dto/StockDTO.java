package building.sum.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import building.sum.inventory.model.Market;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StockDTO {

	private Long stockId;
	private String stockName;
	private String stockSymbol;
	private Market boughtMarket;
	private Integer quantity;
	private LocalDateTime investmentDate;
	private BigDecimal buyPrice;

	@Override
	public String toString() {
		return stockId + "," + stockName + "," + stockSymbol + "," + boughtMarket + "," + quantity + ","
				+ investmentDate + "," + buyPrice;
	}

}
