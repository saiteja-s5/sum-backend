package building.sum.inventory.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.inventory.dto.DividendDTO;
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
	public DividendDTO getDividend(String userJoinKey, Long dividendId) {
		try {
			Optional<Dividend> dividendContainer = dividendRepository.findByUserJoinKeyAndDividendId(userJoinKey,
					dividendId);
			if (dividendContainer.isPresent()) {
				Dividend dividend = dividendContainer.get();
				return dividend2DTO(dividend);
			} else {
				throw new ResourceNotFoundException(String.format("Dividend with Id - %d not found", dividendId));
			}
		} catch (Exception e) {
			log.error("Dividend with Id - {} not found", dividendId);
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	@Override
	public List<DividendDTO> getDividends(String userJoinKey) {
		try {
			List<Dividend> savedDividends = dividendRepository.findAllByUserJoinKey(userJoinKey);
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
	public void deleteDividend(String userJoinKey, Long dividendId) {
		try {
			Optional<Dividend> savedDividend = dividendRepository.findByUserJoinKeyAndDividendId(userJoinKey,
					dividendId);
			if (savedDividend.isPresent()) {
				dividendRepository.deleteByUserJoinKeyAndDividendId(userJoinKey, dividendId);
			} else {
				log.warn("Requested dividend with Id - {} not found", dividendId);
				throw new ResourceNotFoundException(String.format("Dividend with Id - %d not found", dividendId));
			}
		} catch (Exception e) {
			log.error("Dividend with Id - {} not deleted", dividendId);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	@Override
	public void deleteDividends(String userJoinKey) {
		try {
			List<Dividend> savedDividends = dividendRepository.findAllByUserJoinKey(userJoinKey);
			if (!savedDividends.isEmpty()) {
				dividendRepository.deleteByUserJoinKey(userJoinKey);
			} else {
				log.warn("Requested dividends for user - {} not found", userJoinKey);
				throw new ResourceNotFoundException(String.format("Dividends for user - %s not found", userJoinKey));
			}
		} catch (Exception e) {
			log.error("Dividends for user - {} not deleted", userJoinKey);
			throw new ResourceNotDeletedException(e.getMessage());
		}
	}

	private DividendDTO dividend2DTO(Dividend dividend) {
		return DividendDTO.builder().dividendId(dividend.getDividendId()).companyName(dividend.getCompanyName())
				.companySymbol(dividend.getCompanySymbol()).creditedDate(dividend.getCreditedDate())
				.creditedAmount(dividend.getCreditedAmount()).market(dividend.getMarket()).build();
	}
}
