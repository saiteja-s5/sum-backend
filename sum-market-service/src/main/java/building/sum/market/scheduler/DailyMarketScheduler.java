package building.sum.market.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import building.sum.market.dto.StockDashboardDTO;
import building.sum.market.model.SchedulerType;
import building.sum.market.service.DashboardService;
import building.sum.market.utility.SchedulerLogWriter;

@Component
public class DailyMarketScheduler {

	@Value("${daily-market.scheduler-enabled}")
	private boolean isDailySchedulerEnabled;

	private static final Logger log = LogManager.getLogger();

	@Autowired
	private DashboardService dashboardService;

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
				StockDashboardDTO holdings = dashboardService.getCurrentHoldings();
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
