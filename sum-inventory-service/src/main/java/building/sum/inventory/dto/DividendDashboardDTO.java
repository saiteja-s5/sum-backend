package building.sum.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DividendDashboardDTO {

	private List<DividendDashboardRowDTO> dividends;
	private BigDecimal totalDividendEarned;
	private String highestDividendCompany;
	private LocalDateTime dividendLastTransactionOn;
	private LocalDateTime dividendTableUpdatedOn;

	@Override
	public String toString() {
		return dividends + "," + totalDividendEarned + "," + highestDividendCompany + "," + dividendLastTransactionOn
				+ "," + dividendTableUpdatedOn;
	}

}
