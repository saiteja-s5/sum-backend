package building.sum.market.scheduler;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import building.sum.market.exception.SchedulerStoppedException;
import building.sum.market.model.SchedulerType;
import building.sum.market.repository.NotificationSubscriptionsRepository;
import building.sum.market.utility.SchedulerLogWriter;

@Component
public class AfterMarketScheduler {

	@Value("${after-market.scheduler-enabled}")
	private boolean isDailySchedulerEnabled;

	private static final Logger log = LogManager.getLogger();

	private final SchedulerLogWriter logWriter;

	private final WebClient.Builder builder;

	private final NotificationSubscriptionsRepository subscriptions;

	public AfterMarketScheduler(SchedulerLogWriter logWriter, Builder builder,
			NotificationSubscriptionsRepository subscriptions) {
		this.logWriter = logWriter;
		this.builder = builder;
		this.subscriptions = subscriptions;
	}

	@Scheduled(cron = "${after-market.scheduler-time}")
	public void getDailyDataForCurrentHoldings() {
		SchedulerType type = SchedulerType.DAILY_MARKET;
		try {
			if (isDailySchedulerEnabled) {
				String logContent = "After Market Scheduler is Enabled";
				logWriter.writeLog(logContent, type);
				log.info(logContent);
				List<String> subscribedUsers = subscriptions.findBySchedulerType(type.toString()).stream()
						.filter(sub -> sub.getIsActive() == 1).map(sub -> sub.getUserJoinKey()).toList();
				for (String user : subscribedUsers) {
					logContent = String.format("Fetching Daily data of Current Holdings of user - %s", user);
					logWriter.writeLog(logContent, type);
					log.info(logContent);
					byte[] attachment = builder.build().get()
							.uri("http://localhost:9595/pdf-reports/after-market/" + user).retrieve()
							.bodyToMono(byte[].class).block();
					String logIntermediate = String.format("Pdf file received from report service for user - %s", user);
					logWriter.writeLog(logIntermediate, type);
					log.info(logIntermediate);
					if (attachment != null) {
						builder.build().post().uri("http://localhost:9595/email-notify/with-pdf-attachment/" + user)
								.body(BodyInserters.fromValue(attachment)).retrieve().bodyToMono(Void.class).block();
						logContent = String.format("Fetching of Daily data completed for user - %s", user);
						logWriter.writeLog(logContent, type);
						log.info(logContent);
					} else {
						String errorLog = String.format("Null received from report service for user - %s", user);
						logWriter.writeLog(errorLog, type);
						log.warn(errorLog);
					}
				}
			} else {
				String logDisableContent = "Daily Scheduler is disabled";
				logWriter.writeLog(logDisableContent, type);
				log.warn(logDisableContent);
			}
		} catch (Exception e) {
			log.warn("Scheduler stopped!");
			throw new SchedulerStoppedException(e.getMessage());
		}
	}

}