package building.sum.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

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
	private Integer quantity;
	private LocalDate investmentDate;
	private BigDecimal buyPrice;

}
