package building.sum.inventory.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class SumInventoryServiceConfiguration {

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
