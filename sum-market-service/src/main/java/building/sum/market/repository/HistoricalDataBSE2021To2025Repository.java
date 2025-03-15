package building.sum.market.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.HistoricalDataBSE2021To2025;

public interface HistoricalDataBSE2021To2025Repository extends JpaRepository<HistoricalDataBSE2021To2025, Long> {

	Optional<HistoricalDataBSE2021To2025> findTopBySymbolOrderByTradedDateDesc(String symbol);

}
