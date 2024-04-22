package building.sum.inventory.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.inventory.dto.DividendDTO;
import building.sum.inventory.dto.DividendDashboardDTO;
import building.sum.inventory.dto.DividendDashboardRowDTO;
import building.sum.inventory.exception.ResourceNotDeletedException;
import building.sum.inventory.exception.ResourceNotFoundException;
import building.sum.inventory.exception.ResourceNotPostedException;
import building.sum.inventory.model.Dividend;
import building.sum.inventory.repository.DividendRepository;
import building.sum.inventory.service.DividendService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DividendServiceImpl implements DividendService {

	private static final Logger log = LogManager.getLogger();

	private final DividendRepository dividendRepository;

	@Override
	public void postDividend(Dividend dividend) {
		try {
			dividendRepository.save(dividend);
		} catch (Exception e) {
			log.error("Dividend - {} not posted", dividend.getCompanySymbol());
			throw new ResourceNotPostedException(e.getMessage());
		}
	}

	@Override
	public DividendDTO getDividend(Long dividendId) {
		try {
			Optional<Dividend> dividendContainer = dividendRepository.findById(dividendId);
			if (dividendContainer.isPresent()) {
				Dividend dividend = dividendContainer.get();
				return dividend2DTO(dividend);
			} else {
				throw new ResourceNotFoundException(String.format("Dividend with Id - %d not found"));
			}
		} catch (Exception e) {
			log.error("Dividend with Id - {} not found", dividendId);
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<DividendDTO> getDividends() {
		try {
			List<Dividend> savedDividends = dividendRepository.findAll();
			if (savedDividends.isEmpty()) {
				log.warn("No dividends found");
				return new ArrayList<>();
			}
			return savedDividends.stream().map(this::dividend2DTO).toList();
		} catch (Exception e) {
			log.error("Unable to fetch dividends");
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public void deleteDividend(Long dividendId) {
		try {
			Optional<Dividend> savedDividend = dividendRepository.findById(dividendId);
			if (savedDividend.isPresent()) {
				dividendRepository.deleteById(dividendId);
			} else {
				log.warn("Requested dividend with Id - {} not found", dividendId);
				throw new ResourceNotFoundException(String.format("Dividend with Id - %d not found"));
			}
		} catch (Exception e) {
			log.error("Dividend with Id - {} not deleted", dividendId);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	// TODO 1 Values need to be filled after market integration is done
	@Override
	public DividendDashboardDTO getCurrentEarnings() {
		try {
			List<DividendDashboardRowDTO> dividends = dividendRepository.findAll().stream()
					.map(DividendDashboardRowDTO::new).toList();
			if (!dividends.isEmpty()) {
				return DividendDashboardDTO.builder().dividends(dividends)
						.totalDividendEarned(BigDecimal
								.valueOf(dividends.stream().map(dividend -> dividend.getCreditedAmount().doubleValue())
										.reduce(0.0, (v1, v2) -> v1 + v2)))
						.highestDividendCompany(
								dividends.stream().max(Comparator.comparing(DividendDashboardRowDTO::getCreditedAmount))
										.get().getCompanyName())
						.dividendLastTransactionOn(
								dividends.stream().max(Comparator.comparing(DividendDashboardRowDTO::getCreditedDate))
										.get().getCreditedDate())
						.dividendTableUpdatedOn(null).build();
			} else {
				log.warn("No dividends found");
				return DividendDashboardDTO.builder().build();
			}
		} catch (Exception e) {
			log.error("Unable to fetch dividends");
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	private DividendDTO dividend2DTO(Dividend dividend) {
		return DividendDTO.builder().dividendId(dividend.getDividendId()).companyName(dividend.getCompanyName())
				.companySymbol(dividend.getCompanySymbol()).creditedDate(dividend.getCreditedDate())
				.creditedAmount(dividend.getCreditedAmount()).build();
	}
}
