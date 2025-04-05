package building.sum.report.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SumReportServiceConfiguration {

	@Value("${daily-after-market-queue}")
	private String dailyAfterMarketQueue;

	@Value("${daily-after-market-exchange}")
	private String dailyAfterMarketExchange;

	@Value("${daily-after-market-key}")
	private String dailyAfterMarketKey;

	@Bean
	@LoadBalanced
	WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

	/* Configuration for reading messages from external file - Starts */
	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages_en");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean validatorBean = new LocalValidatorFactoryBean();
		validatorBean.setValidationMessageSource(messageSource());
		return validatorBean;
	}
	/* Configuration for reading messages from external file - Ends */

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
