package building.sum.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import building.sum.inventory.model.Dividend;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DividendDashboardRowDTO {

	private String companyName;
	private LocalDateTime creditedDate;
	private BigDecimal creditedAmount;

	public DividendDashboardRowDTO(Dividend dividend) {
		this.companyName = dividend.getCompanyName();
		this.creditedDate = dividend.getCreditedDate();
		this.creditedAmount = dividend.getCreditedAmount();
	}

	@Override
	public String toString() {
		return companyName + "," + creditedDate + "," + creditedAmount;
	}

}
