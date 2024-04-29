package building.sum.gateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SumRoutesConfiguration {

	@Bean
	RouteLocator sumMicroserviceRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("sum-inventory-service",
						r -> r.path("/stocks/**", "/funds/**", "/dividends/**").uri("lb://sum-inventory-service"))
				.route("sum-market-service", r -> r.path("/daily-market/**").uri("lb://sum-market-service")).build();
	}

}
