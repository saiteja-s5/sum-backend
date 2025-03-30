package building.sum.market.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SumMarketServiceConfiguration {

	/* Configuration to handle responses upto 10MB */
	@Bean
	WebClient.Builder webClientBuilder() {
		return WebClient.builder().codecs(
				clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024));
	}

	@Bean
	@LoadBalanced
	WebClient.Builder webClientBuilderLoadBalanced() {
		return WebClient.builder();
	}

	/* Configuration for rabbitmq - Starts */
	@Bean
	Queue queue() {
		return new Queue("daily-after-market-queue");
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("daily-after-market-exchange");
	}

	@Bean
	Binding binding() {
		return BindingBuilder.bind(queue()).to(exchange()).with("daily-after-market-key");
	}
	/* Configuration for rabbitmq - Ends */

}
