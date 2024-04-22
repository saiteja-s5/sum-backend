package building.sum.market.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("request")
public class YahooFinanceApiRequestProperties {

	private String crumb;
	private String cookie;
	private String scheme;
	private String host;
	private String quotePath;
	private String historicalQuotePath;

}
