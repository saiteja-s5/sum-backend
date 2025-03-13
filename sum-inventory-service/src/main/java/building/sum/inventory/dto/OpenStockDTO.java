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
public class OpenStockDTO {

	private Long openStockId;
	private String stockName;
	private String stockSymbol;
	private Market boughtMarket;
	private LocalDateTime investmentDate;
	private Integer quantity;
	private BigDecimal buyPrice;

	@Override
	public String toString() {
		return openStockId + "," + stockName + "," + stockSymbol + "," + boughtMarket + "," + quantity + ","
				+ investmentDate + "," + buyPrice;
	}

}
