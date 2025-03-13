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
				.route("sum-service-registry",
						r -> r.path("/eureka/web").filters(f -> f.setPath("/")).uri("lb://sum-service-registry"))
				.route("sum-inventory-service",
						r -> r.path("/open-stocks/**", "/funds/**", "/dividends/**", "/closed-stocks/**")
								.uri("lb://sum-inventory-service"))
				.route("sum-market-service", r -> r.path("/market/**").uri("lb://sum-market-service")).build();
	}

}
