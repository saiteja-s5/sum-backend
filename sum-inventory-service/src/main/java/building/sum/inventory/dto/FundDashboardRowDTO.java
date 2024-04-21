package building.sum.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import building.sum.inventory.model.Fund;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundDashboardRowDTO {

	private LocalDateTime transactionDate;
	private BigDecimal creditedAmount;
	private BigDecimal debitedAmount;
	private BigDecimal amountInOut;

	public FundDashboardRowDTO(Fund fund) {
		this.transactionDate = fund.getTransactionDate();
		this.creditedAmount = fund.getCreditedAmount();
		this.debitedAmount = fund.getDebitedAmount();
		this.amountInOut = fund.getCreditedAmount().subtract(fund.getDebitedAmount());
	}

	@Override
	public String toString() {
		return transactionDate + "," + creditedAmount + "," + debitedAmount + "," + amountInOut;
	}

}
