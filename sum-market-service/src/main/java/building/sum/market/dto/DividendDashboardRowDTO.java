package building.sum.market.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import building.sum.market.model.Dividend;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DividendDashboardRowDTO {

	private String companyName;
	private LocalDateTime creditedDate;
	private BigDecimal creditedAmount;

	public DividendDashboardRowDTO(String companyName, LocalDateTime creditedDate, BigDecimal creditedAmount) {
		super();
		this.companyName = companyName;
		this.creditedDate = creditedDate;
		this.creditedAmount = creditedAmount;
	}

	public DividendDashboardRowDTO(Dividend dividend) {
		this(dividend.getCompanyName(), dividend.getCreditedDate(), dividend.getCreditedAmount());
	}

	@Override
	public String toString() {
		return companyName + "," + creditedDate + "," + creditedAmount;
	}

}
