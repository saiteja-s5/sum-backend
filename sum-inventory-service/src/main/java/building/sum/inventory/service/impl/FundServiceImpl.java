package building.sum.inventory.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.inventory.dto.FundDTO;
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
	public FundDTO getFund(String userJoinKey, Long fundId) {
		try {
			Optional<Fund> fundContainer = fundRepository.findByUserJoinKeyAndFundId(userJoinKey, fundId);
			if (fundContainer.isPresent()) {
				Fund fund = fundContainer.get();
				return fund2DTO(fund);
			} else {
				throw new ResourceNotFoundException(String.format("Fund with Id - %d not found", fundId));
			}
		} catch (Exception e) {
			log.error("Fund with Id - {} not found", fundId);
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<FundDTO> getFunds(String userJoinKey) {
		try {
			List<Fund> savedFunds = fundRepository.findAllByUserJoinKey(userJoinKey);
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
	public void deleteFund(String userJoinKey, Long fundId) {
		try {
			Optional<Fund> savedFund = fundRepository.findByUserJoinKeyAndFundId(userJoinKey, fundId);
			if (savedFund.isPresent()) {
				fundRepository.deleteByUserJoinKeyAndFundId(userJoinKey, fundId);
			} else {
				log.warn("Requested fund with Id - {} not found", fundId);
				throw new ResourceNotFoundException(String.format("Fund with Id - %d not found", fundId));
			}
		} catch (Exception e) {
			log.error("Fund with Id - {} not deleted", fundId);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	private FundDTO fund2DTO(Fund fund) {
		return FundDTO.builder().fundId(fund.getFundId()).transactionDate(fund.getTransactionDate())
				.creditedAmount(fund.getCreditedAmount()).debitedAmount(fund.getDebitedAmount()).build();
	}
}
