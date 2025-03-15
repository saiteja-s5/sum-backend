package building.sum.market.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import building.sum.market.model.Fund;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundDashboardRowDTO {

	private LocalDateTime transactionDate;
	private BigDecimal creditedAmount;
	private BigDecimal debitedAmount;
	private BigDecimal netFundInDematAmount;

	public FundDashboardRowDTO(LocalDateTime transactionDate, BigDecimal creditedAmount, BigDecimal debitedAmount) {
		super();
		this.transactionDate = transactionDate;
		this.creditedAmount = creditedAmount;
		this.debitedAmount = debitedAmount;
		this.netFundInDematAmount = netFundInDematAmount();
	}

	public FundDashboardRowDTO(Fund fund) {
		this(fund.getTransactionDate(), fund.getCreditedAmount(), fund.getDebitedAmount());
	}

	private BigDecimal netFundInDematAmount() {
		return creditedAmount.subtract(debitedAmount);
	}

	@Override
	public String toString() {
		return transactionDate + "," + creditedAmount + "," + debitedAmount + "," + netFundInDematAmount;
	}

}
