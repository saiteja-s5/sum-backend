package building.sum.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FundDTO {

	private Long fundId;
	private LocalDateTime transactionDate;
	private BigDecimal creditedAmount;
	private BigDecimal debitedAmount;

	@Override
	public String toString() {
		return fundId + "," + transactionDate + "," + creditedAmount + "," + debitedAmount;
	}

}
