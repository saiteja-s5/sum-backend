package building.sum.market.scheduler;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import building.sum.market.dto.StockDashboardDTO;
import building.sum.market.model.SchedulerType;
import building.sum.market.service.DailyMarketService;
import building.sum.market.utility.SchedulerLogWriter;
import building.sum.market.utility.SumUtility;

@Component
public class DailyMarketScheduler {

	@Value("${daily-market.scheduler-enabled}")
	private boolean isDailySchedulerEnabled;

	private static final Logger log = LogManager.getLogger();

	@Autowired
	private DailyMarketService marketService;

	@Autowired
	private SchedulerLogWriter logWriter;

	@Scheduled(cron = "${daily-market.scheduler-time}")
	public void getDailyDataForCurrentHoldings() {
		SchedulerType type = SchedulerType.DAILY_MARKET;
		try {
			if (isDailySchedulerEnabled) {
				String logContent = "Daily Market Scheduler is Enabled";
				logWriter.writeLog(logContent, type);
				log.info(logContent);
				logContent = "Fetching Daily data of Current Holdings";
				logWriter.writeLog(logContent, type);
				log.info(logContent);
				// TODO Get current holdings
				StockDashboardDTO holdings = StockDashboardDTO.builder().build();
				double totalInvestmentValue = holdings.getTotalStockInvestmentValue().doubleValue();
				double currentValue = holdings.getStocks().stream()
						.map(stock -> marketService.getQuote(stock.getMarket(), stock.getStockSymbol())
								.getRegularMarketPrice().doubleValue() * stock.getQuantity())
						.reduce(0.0, (p1, p2) -> p1 + p2);
				double currentReturn = currentValue - totalInvestmentValue;
				BigDecimal currentReturnPercentage = SumUtility.getPercentageReturn(totalInvestmentValue, currentValue);
				holdings.setTotalStockCurrentValue(BigDecimal.valueOf(currentValue));
				holdings.setTotalStockCurrentReturn(BigDecimal.valueOf(currentReturn));
				holdings.setTotalStockCurrentReturnPercent(currentReturnPercentage);
				// TODO Send Email with holdings data
				logContent = "Fetching of Daily data completed";
				logWriter.writeLog(logContent, type);
				log.info(logContent);
			} else {
				String logDisableContent = "Daily Scheduler is disabled";
				logWriter.writeLog(logDisableContent, type);
				log.info(logDisableContent);
			}
		} catch (Exception e) {
			// TODO Send Email on Scheduler Failure
		}
	}

}
