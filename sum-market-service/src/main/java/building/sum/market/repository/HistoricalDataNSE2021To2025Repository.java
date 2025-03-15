package building.sum.market.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.HistoricalDataNSE2021To2025;

public interface HistoricalDataNSE2021To2025Repository extends JpaRepository<HistoricalDataNSE2021To2025, Long> {

	Optional<HistoricalDataNSE2021To2025> findTopBySymbolOrderByTradedDateDesc(String symbol);

}
