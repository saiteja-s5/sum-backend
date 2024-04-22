package building.sum.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

	@Override
	public String toString() {
		return dividendId + "," + companyName + "," + companySymbol + "," + creditedDate + "," + creditedAmount;
	}

}
