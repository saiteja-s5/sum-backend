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
						r -> r.path("/eureka/web").filters(f -> f.setPath("/")).uri("http://localhost:9090"))
				.route("sum-service-registry-static-resources", r -> r.path("/eureka/**").uri("http://localhost:9090"))
				.route("sum-inventory-service",
						r -> r.path("/stocks/**", "/funds/**", "/dividends/**").uri("lb://sum-inventory-service"))
				.route("sum-market-service", r -> r.path("/daily-market/**").uri("lb://sum-market-service"))
				.route("sum-report-service", r -> r.path("/pdf-reports/**").uri("lb://sum-report-service")).build();
	}

}
