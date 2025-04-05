package building.sum.market.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SumMarketServiceConfiguration {

	@Value("${daily-after-market-queue}")
	private String dailyAfterMarketQueue;

	@Value("${daily-after-market-exchange}")
	private String dailyAfterMarketExchange;

	@Value("${daily-after-market-key}")
	private String dailyAfterMarketKey;

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
	Queue dailyAfterMarketQueue() {
		return new Queue(dailyAfterMarketQueue);
	}

	@Bean
	TopicExchange dailyAfterMarketExchange() {
		return new TopicExchange(dailyAfterMarketExchange);
	}

	@Bean
	Binding dailyAfterMarketBinding() {
		return BindingBuilder.bind(dailyAfterMarketQueue()).to(dailyAfterMarketExchange()).with(dailyAfterMarketKey);
	}
	/* Configuration for rabbitmq - Ends */

}
