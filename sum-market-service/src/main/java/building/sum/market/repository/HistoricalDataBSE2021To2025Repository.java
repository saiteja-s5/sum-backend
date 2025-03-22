package building.sum.market.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.HistoricalDataBSE2021To2025;

public interface HistoricalDataBSE2021To2025Repository extends JpaRepository<HistoricalDataBSE2021To2025, Long> {

	Optional<HistoricalDataBSE2021To2025> findTopBySymbolOrderByTradedDateDesc(String symbol);

	List<HistoricalDataBSE2021To2025> findBySymbolOrderByTradedDateAsc(String symbol);

	List<HistoricalDataBSE2021To2025> findBySymbolAndTradedDateGreaterThanEqualOrderByTradedDateAsc(String symbol,
			LocalDateTime tradedDate);

	List<HistoricalDataBSE2021To2025> findBySymbolAndTradedDateBetweenOrderByTradedDateAsc(String symbol,
			LocalDateTime startDate, LocalDateTime endDate);

}
