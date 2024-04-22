package building.sum.market;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
public class SumMarketServiceApplication {

	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {
		SpringApplication.run(SumMarketServiceApplication.class, args);
		log.info("SUM Wealth Monitoring App Market Integration Service started");
	}

}
