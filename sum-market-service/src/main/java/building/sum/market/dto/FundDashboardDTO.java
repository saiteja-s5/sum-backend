package building.sum.market.dto;

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
	private BigDecimal totalFundsValue;
	private BigDecimal totalCreditedAmountValue;
	private BigDecimal totalDebitedAmountValue;
	private LocalDateTime fundLastTransactionOn;
	private LocalDateTime fundTableUpdatedOn;

	@Override
	public String toString() {
		return funds + "," + totalFundsValue + "," + totalCreditedAmountValue + "," + totalDebitedAmountValue + ","
				+ fundLastTransactionOn + "," + fundTableUpdatedOn;
	}

}
