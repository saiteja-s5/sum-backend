package building.sum.service_registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SumServiceRegistryApplication {

	private static final Logger log = LogManager.getLogger();

	public static void main(String[] args) {
		SpringApplication.run(SumServiceRegistryApplication.class, args);
		log.info("SUM Wealth Monitoring App Eureka Server started");
	}

}
