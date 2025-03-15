package building.sum.market.scheduler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import building.sum.market.exception.SchedulerStoppedException;
import building.sum.market.model.SchedulerType;
import building.sum.market.service.MarketService;
import building.sum.market.utility.SchedulerLogWriter;
import building.sum.market.utility.SumUtility;

@Component
public class AfterMarketScheduler {

	@Value("${daily-after-market-table-update.scheduler-enabled}")
	private boolean isDailyAfterMarketUpdaterSchedulerEnabled;

	private static final Logger log = LogManager.getLogger();

	private static final DateTimeFormatter DMY_FORMATTER = SumUtility.DMY_FORMATTER;

	private final SchedulerLogWriter logWriter;

	private final MarketService marketService;

	public AfterMarketScheduler(SchedulerLogWriter logWriter, MarketService marketService) {
		this.logWriter = logWriter;
		this.marketService = marketService;
	}

	@Scheduled(cron = "${daily-after-market-table-update.scheduler-time}")
	public void getDailyAfterMarketUpdateData() {
		SchedulerType type = SchedulerType.DAILY_AFTER_MARKET;
		try {
			if (isDailyAfterMarketUpdaterSchedulerEnabled) {
				String logContent = "Daily After Market Updater Scheduler is Enabled";
				logWriter.writeLog(logContent, type);
				log.info(logContent);
				String today = LocalDate.now().format(DMY_FORMATTER);
				String tomorrow = LocalDate.now().plusDays(1).format(DMY_FORMATTER);
				marketService.saveHistoricalStockQuote(today, tomorrow);
			} else {
				String logDisableContent = "Daily After Market Updater Scheduler is Disabled";
				logWriter.writeLog(logDisableContent, type);
				log.warn(logDisableContent);
			}
		} catch (Exception e) {
			log.warn("Scheduler stopped! Type - {}", type);
			throw new SchedulerStoppedException(e.getMessage());
		}
	}

}