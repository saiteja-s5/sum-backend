package building.sum.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SumReportServiceApplication {

	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {
		SpringApplication.run(SumReportServiceApplication.class, args);
		log.info("SUM Wealth Monitoring App Reports Service started");
	}

}
