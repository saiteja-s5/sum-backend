package building.sum.market.service.impl;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import building.sum.market.dto.YahooFinanceResponseDTO;
import building.sum.market.dto.YahooQuoteDTO;
import building.sum.market.exception.ResourceNotFetchedException;
import building.sum.market.model.Market;
import building.sum.market.model.YahooFinanceApiRequestProperties;
import building.sum.market.service.DailyMarketService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DailyMarketServiceImpl implements DailyMarketService {

	private static final Logger log = LogManager.getLogger();

	private final YahooFinanceApiRequestProperties requestProperties;

	private final WebClient.Builder builder;

	@Override
	public YahooQuoteDTO getQuote(Market market, String symbol) {
		URIBuilder uriBuilder = new URIBuilder().setScheme(requestProperties.getScheme())
				.setHost(requestProperties.getHost()).setPath(requestProperties.getQuotePath())
				.addParameter("symbols", symbol + market.getExtension())
				.addParameter("crumb", requestProperties.getCrumb());
		try {
			YahooFinanceResponseDTO response = builder.build().get().uri(uriBuilder.build())
					.header("Cookie", requestProperties.getCookie()).retrieve()
					.bodyToMono(YahooFinanceResponseDTO.class).block();
			YahooQuoteDTO fetchedResponse;
			if (response != null) {
				fetchedResponse = response.getQuoteResponse().getResult().get(0);
				log.debug("Response mapping to DTO is completed");
				return fetchedResponse;
			} else {
				log.warn("Exception occurred while fetching stock - {}", symbol);
				throw new ResourceNotFetchedException(String.format("Quote - %s not fetched", symbol));
			}
		} catch (Exception e) {
			throw new ResourceNotFetchedException(e.getMessage());
		}
	}

}
