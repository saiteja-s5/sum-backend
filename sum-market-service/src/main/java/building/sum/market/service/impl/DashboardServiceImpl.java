package building.sum.market.service.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import building.sum.market.dto.ClosedStockDashboardDTO;
import building.sum.market.dto.ClosedStockDashboardRowDTO;
import building.sum.market.dto.DividendDashboardDTO;
import building.sum.market.dto.DividendDashboardRowDTO;
import building.sum.market.dto.FundDashboardDTO;
import building.sum.market.dto.FundDashboardRowDTO;
import building.sum.market.dto.OpenStockDashboardDTO;
import building.sum.market.dto.OpenStockDashboardRowDTO;
import building.sum.market.exception.ResourceNotFoundException;
import building.sum.market.model.TableLastUpdateDetails;
import building.sum.market.repository.ClosedStockRepository;
import building.sum.market.repository.DividendRepository;
import building.sum.market.repository.FundRepository;
import building.sum.market.repository.OpenStockRepository;
import building.sum.market.repository.TableLastUpdateDetailsRepository;
import building.sum.market.service.DashboardService;
import building.sum.market.service.MarketService;
import building.sum.market.utility.SumUtility;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

	private static final Logger log = LogManager.getLogger();

	private static final String LAST_UPDATED_TABLE_PK = SumUtility.LAST_UPDATED_TABLE_PK;

	private final OpenStockRepository openStockRepository;

	private final FundRepository fundRepository;

	private final DividendRepository dividendRepository;

	private final ClosedStockRepository closedStockRepository;

	private final TableLastUpdateDetailsRepository tableLastUpdateDetailsRepository;

	private final MarketService marketService;

	@Override
	public OpenStockDashboardDTO getOpenStockHoldings(String userJoinkey) {
		log.debug(">>>>> getOpenStockHoldings args - {}", userJoinkey);
		try {
			List<OpenStockDashboardRowDTO> openStocks = openStockRepository.findAllByUserJoinKey(userJoinkey).stream()
					.map(OpenStockDashboardRowDTO::new).sorted((s1, s2) -> s1.getBuyDate().compareTo(s2.getBuyDate()))
					.toList();
			if (!openStocks.isEmpty()) {
				double totalInvestmentValue = openStocks.stream().map(stock -> stock.getBuyValue().doubleValue())
						.reduce(0.0, (v1, v2) -> v1 + v2);
				double currentValue = openStocks.stream()
						.map(stock -> marketService.getQuote(stock.getMarket(), stock.getStockSymbol())
								.getRegularMarketPrice().doubleValue() * stock.getQuantity())
						.reduce(0.0, (p1, p2) -> p1 + p2);
				double currentReturn = currentValue - totalInvestmentValue;
				BigDecimal currentReturnPercentage = SumUtility.getPercentageReturn(totalInvestmentValue, currentValue);
				Optional<OpenStockDashboardRowDTO> latestBuyDateContainer = openStocks.stream()
						.max(Comparator.comparing(OpenStockDashboardRowDTO::getBuyDate));
				Optional<TableLastUpdateDetails> updatedDateContainer = tableLastUpdateDetailsRepository
						.findById(LAST_UPDATED_TABLE_PK);
				return OpenStockDashboardDTO.builder().openStocks(openStocks)
						.totalStockInvestmentValue(SumUtility.roundTo(totalInvestmentValue, 2))
						.totalStockCurrentValue(SumUtility.roundTo(currentValue, 2))
						.totalStockCurrentReturn(SumUtility.roundTo(currentReturn, 2))
						.totalStockCurrentReturnPercent(SumUtility.roundTo(currentReturnPercentage.doubleValue(), 2))
						.openStockLastTransactionOn(
								latestBuyDateContainer.isPresent() ? latestBuyDateContainer.get().getBuyDate() : null)
						.openStockTableUpdatedOn(updatedDateContainer.isPresent()
								? updatedDateContainer.get().getOpenStockHoldingsUpdatedDateTime()
								: null)
						.build();
			} else {
				log.warn("No open stocks found for user - {}", userJoinkey);
				return OpenStockDashboardDTO.builder().build();
			}
		} catch (Exception e) {
			log.error("Unable to fetch open stocks for user - {}", userJoinkey);
			throw new ResourceNotFoundException(e.getMessage());
		} finally {
			log.debug("<<<<< getOpenStockHoldings args - {}", userJoinkey);
		}
	}

	@Override
	public FundDashboardDTO getFunds(String userJoinkey) {
		log.debug(">>>>> getFunds args - {}", userJoinkey);
		try {
			List<FundDashboardRowDTO> funds = fundRepository.findAllByUserJoinKey(userJoinkey).stream()
					.map(FundDashboardRowDTO::new)
					.sorted((f1, f2) -> f1.getTransactionDate().compareTo(f2.getTransactionDate())).toList();
			if (!funds.isEmpty()) {
				double totalFundsValue = funds.stream().map(fund -> fund.getNetFundInDematAmount().doubleValue())
						.reduce(0.0, (v1, v2) -> v1 + v2);
				double totalCreditedAmountValue = funds.stream().map(fund -> fund.getCreditedAmount().doubleValue())
						.reduce(0.0, (v1, v2) -> v1 + v2);
				double totalDebitedAmountValue = funds.stream().map(fund -> fund.getDebitedAmount().doubleValue())
						.reduce(0.0, (v1, v2) -> v1 + v2);
				Optional<FundDashboardRowDTO> latestBuyDateContainer = funds.stream()
						.max(Comparator.comparing(FundDashboardRowDTO::getTransactionDate));
				Optional<TableLastUpdateDetails> updatedDateContainer = tableLastUpdateDetailsRepository
						.findById(LAST_UPDATED_TABLE_PK);
				return FundDashboardDTO.builder().funds(funds).totalFundsValue(SumUtility.roundTo(totalFundsValue, 2))
						.totalCreditedAmountValue(SumUtility.roundTo(totalCreditedAmountValue, 2))
						.totalDebitedAmountValue(SumUtility.roundTo(totalDebitedAmountValue, 2))
						.fundLastTransactionOn(
								latestBuyDateContainer.isPresent() ? latestBuyDateContainer.get().getTransactionDate()
										: null)
						.fundTableUpdatedOn(
								updatedDateContainer.isPresent() ? updatedDateContainer.get().getFundUpdatedDateTime()
										: null)
						.build();
			} else {
				log.warn("No funds found for user - {}", userJoinkey);
				return FundDashboardDTO.builder().build();
			}
		} catch (Exception e) {
			log.error("Unable to fetch funds for user - {}", userJoinkey);
			throw new ResourceNotFoundException(e.getMessage());
		} finally {
			log.debug("<<<<< getFunds args - {}", userJoinkey);
		}
	}

	@Override
	public DividendDashboardDTO getDividends(String userJoinkey) {
		log.debug(">>>>> getDividends args - {}", userJoinkey);
		try {
			List<DividendDashboardRowDTO> dividends = dividendRepository.findAllByUserJoinKey(userJoinkey).stream()
					.map(DividendDashboardRowDTO::new)
					.sorted((d1, d2) -> d1.getCreditedDate().compareTo(d2.getCreditedDate())).toList();
			if (!dividends.isEmpty()) {
				double totalDividendsValue = dividends.stream()
						.map(dividend -> dividend.getCreditedAmount().doubleValue()).reduce(0.0, (v1, v2) -> v1 + v2);
				Optional<DividendDashboardRowDTO> latestBuyDateContainer = dividends.stream()
						.max(Comparator.comparing(DividendDashboardRowDTO::getCreditedDate));
				Optional<TableLastUpdateDetails> updatedDateContainer = tableLastUpdateDetailsRepository
						.findById(LAST_UPDATED_TABLE_PK);
				return DividendDashboardDTO.builder().dividends(dividends)
						.totalDividendsValue(SumUtility.roundTo(totalDividendsValue, 2))
						.dividendLastTransactionOn(
								latestBuyDateContainer.isPresent() ? latestBuyDateContainer.get().getCreditedDate()
										: null)
						.dividendTableUpdatedOn(updatedDateContainer.isPresent()
								? updatedDateContainer.get().getDividendUpdatedDateTime()
								: null)
						.build();
			} else {
				log.warn("No dividends found for user - {}", userJoinkey);
				return DividendDashboardDTO.builder().build();
			}
		} catch (Exception e) {
			log.error("Unable to fetch dividends for user - {}", userJoinkey);
			throw new ResourceNotFoundException(e.getMessage());
		} finally {
			log.debug("<<<<< getDividends args - {}", userJoinkey);
		}
	}

	@Override
	public ClosedStockDashboardDTO getClosedStockHoldings(String userJoinkey) {
		log.debug(">>>>> getClosedStockHoldings args - {}", userJoinkey);
		try {
			List<ClosedStockDashboardRowDTO> closedStocks = closedStockRepository.findAllByUserJoinKey(userJoinkey)
					.stream().map(ClosedStockDashboardRowDTO::new)
					.sorted((c1, c2) -> c1.getSellDate().compareTo(c2.getSellDate())).toList();
			if (!closedStocks.isEmpty()) {
				double totalBoughtAmountValue = closedStocks.stream().map(stock -> stock.getBuyValue().doubleValue())
						.reduce(0.0, (v1, v2) -> v1 + v2);
				double totalSellAmountValue = closedStocks.stream().map(stock -> stock.getSellValue().doubleValue())
						.reduce(0.0, (v1, v2) -> v1 + v2);
				double totalProfitLossValue = totalSellAmountValue - totalBoughtAmountValue;
				double percentReturnPerTransaction = SumUtility
						.getPercentageReturn(totalBoughtAmountValue, totalSellAmountValue).doubleValue()
						/ closedStocks.size();
				Optional<ClosedStockDashboardRowDTO> latestBuyDateContainer = closedStocks.stream()
						.max(Comparator.comparing(ClosedStockDashboardRowDTO::getSellDate));
				Optional<TableLastUpdateDetails> updatedDateContainer = tableLastUpdateDetailsRepository
						.findById(LAST_UPDATED_TABLE_PK);
				return ClosedStockDashboardDTO.builder().closedStocks(closedStocks)
						.totalBoughtAmountValue(SumUtility.roundTo(totalBoughtAmountValue, 2))
						.totalSoldAmountValue(SumUtility.roundTo(totalSellAmountValue, 2))
						.totalProfitLossValue(SumUtility.roundTo(totalProfitLossValue, 2))
						.totalProfitLossPercentage(SumUtility.roundTo(SumUtility
								.getPercentageReturn(totalBoughtAmountValue, totalSellAmountValue).doubleValue(), 2))
						.percentReturnPerTransaction(SumUtility.roundTo(percentReturnPerTransaction, 2))
						.closedStockLastTransactionOn(
								latestBuyDateContainer.isPresent() ? latestBuyDateContainer.get().getSellDate() : null)
						.closedStockTableUpdatedOn(updatedDateContainer.isPresent()
								? updatedDateContainer.get().getClosedStockHoldingsUpdatedDateTime()
								: null)
						.build();
			} else {
				log.warn("No closed stocks found for user - {}", userJoinkey);
				return ClosedStockDashboardDTO.builder().build();
			}
		} catch (Exception e) {
			log.error("Unable to fetch closed stocks for user - {}", userJoinkey);
			throw new ResourceNotFoundException(e.getMessage());
		} finally {
			log.debug("<<<<< getClosedStockHoldings args - {}", userJoinkey);
		}
	}

}
