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
public class DividendDTO {

	private Long dividendId;
	private String companyName;
	private String companySymbol;
	private LocalDateTime creditedDate;
	private BigDecimal creditedAmount;
	private Market market;

	@Override
	public String toString() {
		return dividendId + "," + companyName + "," + companySymbol + "," + market + "," + creditedDate + ","
				+ creditedAmount;
	}

}
