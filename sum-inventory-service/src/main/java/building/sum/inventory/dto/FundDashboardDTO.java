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
public class FundDashboardDTO {

	private List<FundDashboardRowDTO> funds;
	private BigDecimal totalCreditedAmount;
	private BigDecimal totalDebitedAmount;
	private LocalDateTime fundLastTransactionOn;
	private LocalDateTime fundTableUpdatedOn;

	@Override
	public String toString() {
		return funds + "," + totalCreditedAmount + "," + totalDebitedAmount + "," + fundLastTransactionOn + ","
				+ fundTableUpdatedOn;
	}

}
