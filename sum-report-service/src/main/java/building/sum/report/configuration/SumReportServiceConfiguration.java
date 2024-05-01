package building.sum.report.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SumReportServiceConfiguration {

	@Bean
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

}
