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
public class ClosedStockDTO {

	private Long closedStockId;
	private String stockName;
	private String stockSymbol;
	private Market boughtMarket;
	private LocalDateTime buyDate;
	private Integer buyQuantity;
	private BigDecimal buyPrice;
	private LocalDateTime sellDate;
	private Integer sellQuantity;
	private BigDecimal sellPrice;

	@Override
	public String toString() {
		return closedStockId + "," + stockName + "," + stockSymbol + "," + boughtMarket + "," + buyQuantity + ","
				+ buyDate + "," + buyPrice + "," + sellQuantity + "," + sellDate + "," + sellPrice;
	}

}
