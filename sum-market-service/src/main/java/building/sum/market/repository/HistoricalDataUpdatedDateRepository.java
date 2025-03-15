package building.sum.market.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import building.sum.market.model.HistoricalDataUpdatedDate;
import building.sum.market.model.Market;

public interface HistoricalDataUpdatedDateRepository extends JpaRepository<HistoricalDataUpdatedDate, Long> {

	Optional<HistoricalDataUpdatedDate> findByStockSymbolAndMarket(String stockSymbol, Market market);

}
