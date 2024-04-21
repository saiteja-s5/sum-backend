package building.sum.inventory.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.inventory.dto.FundDTO;
import building.sum.inventory.dto.FundDashboardDTO;
import building.sum.inventory.dto.FundDashboardRowDTO;
import building.sum.inventory.exception.ResourceNotDeletedException;
import building.sum.inventory.exception.ResourceNotFoundException;
import building.sum.inventory.exception.ResourceNotPostedException;
import building.sum.inventory.model.Fund;
import building.sum.inventory.repository.FundRepository;
import building.sum.inventory.service.FundService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FundServiceImpl implements FundService {

	private static final Logger log = LogManager.getLogger();

	private final FundRepository fundRepository;

	@Override
	public void postFund(Fund fund) {
		try {
			fundRepository.save(fund);
		} catch (Exception e) {
			log.error("Fund - {} not posted", fund.getCreditedAmount());
			throw new ResourceNotPostedException(e.getMessage());
		}
	}

	@Override
	public FundDTO getFund(Long fundId) {
		try {
			Optional<Fund> fundContainer = fundRepository.findById(fundId);
			if (fundContainer.isPresent()) {
				Fund fund = fundContainer.get();
				return fund2DTO(fund);
			} else {
				throw new ResourceNotFoundException(String.format("Fund with Id - %d not found"));
			}
		} catch (Exception e) {
			log.error("Fund with Id - {} not found", fundId);
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<FundDTO> getFunds() {
		try {
			List<Fund> savedFunds = fundRepository.findAll();
			if (savedFunds.isEmpty()) {
				log.warn("No funds found");
				return new ArrayList<>();
			}
			return savedFunds.stream().map(this::fund2DTO).toList();
		} catch (Exception e) {
			log.error("Unable to fetch funds");
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public void deleteFund(Long fundId) {
		try {
			Optional<Fund> savedFund = fundRepository.findById(fundId);
			if (savedFund.isPresent()) {
				fundRepository.deleteById(fundId);
			} else {
				log.warn("Requested fund with Id - {} not found", fundId);
				throw new ResourceNotFoundException(String.format("Fund with Id - %d not found"));
			}
		} catch (Exception e) {
			log.error("Fund with Id - {} not deleted", fundId);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	// TODO 1 Value need to be filled after market integration is done
	@Override
	public FundDashboardDTO getTillDateFunds() {
		try {
			List<FundDashboardRowDTO> funds = fundRepository.findAll().stream().map(FundDashboardRowDTO::new).toList();
			if (!funds.isEmpty()) {
				return FundDashboardDTO.builder().funds(funds)
						.totalCreditedAmount(BigDecimal.valueOf(funds.stream()
								.map(fund -> fund.getCreditedAmount().doubleValue()).reduce(0.0, (v1, v2) -> v1 + v2)))
						.totalDebitedAmount(BigDecimal.valueOf(funds.stream()
								.map(fund -> fund.getDebitedAmount().doubleValue()).reduce(0.0, (v1, v2) -> v1 + v2)))
						.fundLastTransactionOn(
								funds.stream().max(Comparator.comparing(FundDashboardRowDTO::getTransactionDate)).get()
										.getTransactionDate())
						.fundTableUpdatedOn(null).build();
			} else {
				log.warn("No funds found");
				return FundDashboardDTO.builder().build();
			}
		} catch (Exception e) {
			log.error("Unable to fetch funds");
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	private FundDTO fund2DTO(Fund fund) {
		return FundDTO.builder().fundId(fund.getFundId()).transactionDate(fund.getTransactionDate())
				.creditedAmount(fund.getCreditedAmount()).debitedAmount(fund.getDebitedAmount()).build();
	}
}
