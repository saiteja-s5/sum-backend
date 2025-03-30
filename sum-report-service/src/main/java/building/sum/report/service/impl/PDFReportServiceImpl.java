package building.sum.report.service.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import building.sum.report.dto.OpenStockDashboardDTO;
import building.sum.report.exception.ResourceGenerationFailedException;
import building.sum.report.exception.ResourceNotFoundException;
import building.sum.report.model.AppDetails;
import building.sum.report.model.ReportTemplate;
import building.sum.report.model.ReportTemplateKey;
import building.sum.report.model.User;
import building.sum.report.service.PDFReportService;
import building.sum.report.service.repository.AppDetailsRepository;
import building.sum.report.service.repository.ReportTemplateRepository;
import building.sum.report.service.repository.UserRepository;
import building.sum.report.service.utility.SumUtility;

@Service
public class PDFReportServiceImpl implements PDFReportService {

	private static final Logger log = LogManager.getLogger();

	private static final DateTimeFormatter DMYW_FORMAT = SumUtility.DMYW_FORMAT;

	private static final DateTimeFormatter DMY_WITH_TIME = SumUtility.DMY_WITH_TIME;

	private final WebClient.Builder webClientBuilder;

	private final UserRepository userRepository;

	private final AppDetailsRepository appDetailsRepository;

	private final ReportTemplateRepository reportTemplateRepository;

	@Value("${pdf.author-name}")
	private String author;

	@Value("${pdf.creator}")
	private String creator;

	@Value("${pdf.subject}")
	private String subject;

	@Value("${pdf.title}")
	private String title;

	public PDFReportServiceImpl(WebClient.Builder webClientBuilder, UserRepository userRepository,
			AppDetailsRepository appDetailsRepository, ReportTemplateRepository reportTemplateRepository) {
		this.webClientBuilder = webClientBuilder;
		this.userRepository = userRepository;
		this.appDetailsRepository = appDetailsRepository;
		this.reportTemplateRepository = reportTemplateRepository;
	}

	@Override
	public byte[] generateAfterMarketPdfReport(String userJoinKey) {

		// User Details Fetch
		Optional<User> currentUserContainer = userRepository.findByUserJoinKeyIgnoreCase(userJoinKey);
		User currentUser;
		if (!currentUserContainer.isPresent()) {
			throw new ResourceNotFoundException(String.format("User with Join Key - %s not found", userJoinKey));
		} else {
			currentUser = currentUserContainer.get();
		}

		// App Details Fetch
		Optional<AppDetails> detailsContainer = appDetailsRepository.findById(SumUtility.APP_DETAILS_TABLE_PK);
		AppDetails details;
		if (!detailsContainer.isPresent()) {
			throw new ResourceNotFoundException(
					String.format("App Details with Key - %s not found", SumUtility.APP_DETAILS_TABLE_PK));
		} else {
			details = detailsContainer.get();
		}

		// Report Configuration Fetch
		Optional<ReportTemplate> templateContainer = reportTemplateRepository
				.findById(ReportTemplateKey.DAILY_AFTER_MARKET.getPrimaryKey());
		ReportTemplate template;
		if (!templateContainer.isPresent()) {
			throw new ResourceNotFoundException(String.format("Template Details with Key - %s not found",
					ReportTemplateKey.DAILY_AFTER_MARKET.getPrimaryKey()));
		} else {
			template = templateContainer.get();
		}

		try (PDDocument document = new PDDocument()) {

			PDPage page = new PDPage();
			document.addPage(page);

			float pageWidth = page.getMediaBox().getWidth();
			float pageHeight = page.getMediaBox().getHeight();
			float leftPadding = template.getLeftPadding().floatValue();
			float topPadding = template.getTopPadding().floatValue();

			try (PDPageContentStream stream = new PDPageContentStream(document, page)) {

				PDType1Font headerFont = new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD);
				PDType1Font bodyFont = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
				PDType1Font bodyFontBold = new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD);

				// Set SUM Logo
				PDImageXObject sumIcon = PDImageXObject.createFromByteArray(document, template.getLogo().getData(),
						template.getLogoName());
				stream.drawImage(sumIcon, leftPadding / 2, pageHeight - sumIcon.getHeight() - topPadding);

				// Set Heading
				stream.beginText();
				stream.setFont(headerFont, template.getHeaderFontSize().floatValue());
				stream.newLineAtOffset(pageWidth / 2
						- getTextWidth(template.getHeadingText(), headerFont, template.getHeaderFontSize().intValue())
								/ 2,
						pageHeight - topPadding * 6);
				stream.showText(template.getHeadingText());
				stream.endText();

				// Set User Details
				stream.beginText();
				stream.setLeading(template.getParagraphLineSpacing().floatValue());
				stream.newLineAtOffset(leftPadding, pageHeight - sumIcon.getHeight() - topPadding - 2 * topPadding);

				stream.setFont(bodyFontBold, template.getBodyFontSize().floatValue());
				stream.showText("Name");
				stream.showText(" : ");
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText(currentUser.getFirstName());
				stream.newLine();

				stream.setFont(bodyFontBold, template.getBodyFontSize().floatValue());
				stream.showText("Phone");
				stream.showText(" : ");
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText(currentUser.getContactNumber());
				stream.newLine();

				stream.setFont(bodyFontBold, template.getBodyFontSize().floatValue());
				stream.showText("Email");
				stream.showText(" : ");
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText(currentUser.getEmailId());
				stream.newLine();

				stream.setFont(bodyFontBold, template.getBodyFontSize().floatValue());
				stream.showText("Pancard");
				stream.showText(" : ");
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText(currentUser.getPancardNumber());
				stream.newLine();

				stream.setFont(bodyFontBold, template.getBodyFontSize().floatValue());
				stream.showText("Trade Date");
				stream.showText(" : ");
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText(LocalDate.now().format(DMYW_FORMAT));
				stream.newLine();
				stream.endText();

				// Table Current Holdings

				// 1. Stock Investment Breakdown
				OpenStockDashboardDTO holdings = webClientBuilder.build().get()
						.uri("lb://sum-market-service/dashboard/" + userJoinKey + "/open-stock").retrieve()
						.bodyToMono(OpenStockDashboardDTO.class).block();

				if (holdings != null) {

					stream.beginText();
					String stockHeading = "Stock Investment Breakdown";
					stream.newLineAtOffset(leftPadding,
							pageHeight - sumIcon.getHeight() - topPadding - 17 * topPadding);
					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFontBold, template.getBodyFontSize().floatValue() * 1.1f);
					stream.showText(stockHeading);
					stream.endText();

					stream.beginText();
					stream.newLineAtOffset(leftPadding,
							pageHeight - sumIcon.getHeight() - topPadding - 20 * topPadding);

					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
					stream.showText("Total Investment");
					stream.showText(" : ");
					stream.setNonStrokingColor(Color.BLUE);
					stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
					stream.showText(Optional.ofNullable(holdings.getTotalStockInvestmentValue()).isPresent()
							? holdings.getTotalStockInvestmentValue().toString()
							: "0");
					stream.newLine();

					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
					stream.showText("Current Return");
					stream.showText(" : ");
					stream.setNonStrokingColor(
							holdings.getTotalStockCurrentReturn().doubleValue() > 0 ? Color.GREEN : Color.RED);
					stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentReturn()).isPresent()
							? holdings.getTotalStockCurrentReturn().toString()
							: "0");
					stream.newLine();

					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
					stream.showText("Current Return %");
					stream.showText(" : ");
					stream.setNonStrokingColor(
							holdings.getTotalStockCurrentReturnPercent().doubleValue() > 0 ? Color.GREEN : Color.RED);
					stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentReturnPercent()).isPresent()
							? holdings.getTotalStockCurrentReturnPercent().toString()
							: "0");
					stream.newLine();

					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
					stream.showText("Current Portfolio Value");
					stream.showText(" : ");
					stream.setNonStrokingColor(holdings.getTotalStockCurrentValue().doubleValue() > holdings
							.getTotalStockInvestmentValue().doubleValue() ? Color.GREEN : Color.RED);
					stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentValue()).isPresent()
							? holdings.getTotalStockCurrentValue().toString()
							: "0");
					stream.newLine();

					stream.endText();
				}

				// 2. Mutual Fund Investment Breakdown
				// TODO Mutual Fund Api
//				StockDashboardDTO holdings = webClientBuilder.build().get()
//						.uri("http://localhost:9595/daily-market/" + userJoinKey + "/dashboard").retrieve()
//						.bodyToMono(StockDashboardDTO.class).block();

//				if (holdings != null) {
				/*
				 * stream.beginText(); String mutualFundHeading =
				 * "Mutual Fund Investment Breakdown"; stream.newLineAtOffset(leftPadding,
				 * pageHeight - sumIcon.getHeight() - topPadding - 32 * topPadding);
				 * stream.setNonStrokingColor(Color.BLACK); stream.setFont(bodyFontBold,
				 * template.getBodyFontSize().floatValue() * 1.1f);
				 * stream.showText(mutualFundHeading); stream.endText();
				 * 
				 * stream.beginText(); stream.newLineAtOffset(leftPadding, pageHeight -
				 * sumIcon.getHeight() - topPadding - 35 * topPadding);
				 * 
				 * stream.setNonStrokingColor(Color.BLACK); stream.setFont(bodyFont,
				 * template.getBodyFontSize().floatValue());
				 * stream.showText("Total Investment"); stream.showText(" : ");
				 * stream.setNonStrokingColor(Color.BLUE); stream.setFont(bodyFont,
				 * template.getBodyFontSize().floatValue()); stream.showText("XXX"); //
				 * stream.showText(Optional.ofNullable(holdings.getTotalStockInvestmentValue()).
				 * isPresent() // ? holdings.getTotalStockInvestmentValue().toString() // :
				 * "0"); stream.newLine();
				 * 
				 * stream.setNonStrokingColor(Color.BLACK); stream.setFont(bodyFont,
				 * template.getBodyFontSize().floatValue()); stream.showText("Current Return");
				 * stream.showText(" : "); stream.setNonStrokingColor(
				 * holdings.getTotalStockCurrentReturn().doubleValue() > 0 ? Color.GREEN :
				 * Color.RED); stream.setFont(bodyFont,
				 * template.getBodyFontSize().floatValue()); stream.showText("XXX"); //
				 * stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentReturn()).
				 * isPresent() // ? holdings.getTotalStockCurrentReturn().toString() // : "0");
				 * stream.newLine();
				 * 
				 * stream.setNonStrokingColor(Color.BLACK); stream.setFont(bodyFont,
				 * template.getBodyFontSize().floatValue());
				 * stream.showText("Current Return %"); stream.showText(" : ");
				 * stream.setNonStrokingColor(
				 * holdings.getTotalStockCurrentReturnPercent().doubleValue() > 0 ? Color.GREEN
				 * : Color.RED); stream.setFont(bodyFont,
				 * template.getBodyFontSize().floatValue()); stream.showText("XXX"); //
				 * stream.showText(Optional.ofNullable(holdings.
				 * getTotalStockCurrentReturnPercent()).isPresent() // ?
				 * holdings.getTotalStockCurrentReturnPercent().toString() // : "0");
				 * stream.newLine();
				 * 
				 * stream.setNonStrokingColor(Color.BLACK); stream.setFont(bodyFont,
				 * template.getBodyFontSize().floatValue());
				 * stream.showText("Current Portfolio Value"); stream.showText(" : ");
				 * stream.setNonStrokingColor(holdings.getTotalStockCurrentValue().doubleValue()
				 * > holdings .getTotalStockInvestmentValue().doubleValue() ? Color.GREEN :
				 * Color.RED); stream.setFont(bodyFont,
				 * template.getBodyFontSize().floatValue()); stream.showText("XXX"); //
				 * stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentValue()).
				 * isPresent() // ? holdings.getTotalStockCurrentValue().toString() // : "0");
				 * stream.newLine();
				 * 
				 * stream.endText(); // }
				 */

				// Table Summary

				stream.beginText();
				String summaryHeading = "Overall Investment Breakdown";
				stream.newLineAtOffset(leftPadding, pageHeight - sumIcon.getHeight() - topPadding - 47 * topPadding);
				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFontBold, template.getBodyFontSize().floatValue() * 1.1f);
				stream.showText(summaryHeading);
				stream.endText();

				stream.beginText();
				stream.newLineAtOffset(leftPadding, pageHeight - sumIcon.getHeight() - topPadding - 50 * topPadding);
				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText("Total Investment");
				stream.showText(" : ");
				BigDecimal overallStock = Optional.ofNullable(holdings.getTotalStockInvestmentValue()).isPresent()
						? holdings.getTotalStockInvestmentValue()
						: BigDecimal.ZERO;
				// TODO
				BigDecimal overallMutualFund = BigDecimal.ZERO;
				stream.setNonStrokingColor(Color.BLUE);
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText(overallStock.add(overallMutualFund).toString());
				stream.newLine();

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText("Current Return");
				stream.showText(" : ");
				BigDecimal overallStockCurrent = Optional.ofNullable(holdings.getTotalStockCurrentReturn()).isPresent()
						? holdings.getTotalStockCurrentReturn()
						: BigDecimal.ZERO;
				// TODO
				BigDecimal overallMutualFundCurrent = BigDecimal.ZERO;
				stream.setNonStrokingColor(
						overallStockCurrent.add(overallMutualFundCurrent).doubleValue() > 0 ? Color.GREEN : Color.RED);
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText(overallStockCurrent.add(overallMutualFundCurrent).toString());
				stream.newLine();

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText("Current Return %");
				stream.showText(" : ");
				BigDecimal overallStockCurrentPercent = Optional
						.ofNullable(holdings.getTotalStockCurrentReturnPercent()).isPresent()
								? holdings.getTotalStockCurrentReturnPercent()
								: BigDecimal.ZERO;
				// TODO
				BigDecimal overallMutualFundCurrentPercent = BigDecimal.ZERO;
				stream.setNonStrokingColor(
						overallStockCurrentPercent.add(overallMutualFundCurrentPercent).doubleValue() > 0 ? Color.GREEN
								: Color.RED);
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText(overallStockCurrentPercent.add(overallMutualFundCurrentPercent).toString());
				stream.newLine();

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText("Current Portfolio Value");
				stream.showText(" : ");
				BigDecimal overallStockCurrentValue = Optional.ofNullable(holdings.getTotalStockCurrentValue())
						.isPresent() ? holdings.getTotalStockCurrentValue() : BigDecimal.ZERO;
				// TODO
				BigDecimal overallMutualFundCurrentValue = BigDecimal.ZERO;
				stream.setNonStrokingColor(overallStockCurrentValue.add(overallMutualFundCurrentValue)
						.doubleValue() > overallStock.add(overallMutualFund).doubleValue() ? Color.GREEN : Color.RED);
				stream.setFont(bodyFont, template.getBodyFontSize().floatValue());
				stream.showText(overallStockCurrentValue.add(overallMutualFundCurrentValue).toString());
				stream.newLine();

				stream.endText();

				// Signature
				stream.beginText();
				stream.setNonStrokingColor(Color.BLACK);
				stream.setLeading(template.getParagraphLineSpacingSmall().floatValue());
				stream.newLineAtOffset(leftPadding, 5 * topPadding);
				stream.setFont(bodyFontBold, template.getBodyFontSizeSmall().floatValue());
				stream.showText("Date");
				stream.showText(" : ");
				stream.setFont(bodyFont, template.getBodyFontSizeSmall().floatValue());
				stream.showText(LocalDateTime.now().format(DMY_WITH_TIME));
				stream.newLine();

				stream.setFont(bodyFontBold, template.getBodyFontSizeSmall().floatValue());
				stream.showText("Place");
				stream.showText(" : ");
				stream.setFont(bodyFont, template.getBodyFontSizeSmall().floatValue());
				stream.showText(details.getAppAddressCity());
				stream.newLine();
				stream.endText();

				stream.beginText();
				stream.setLeading(template.getParagraphLineSpacingSmall().floatValue());
				String salutation = "Yours faithfully,";
				stream.newLineAtOffset(pageWidth
						- getTextWidth(salutation, bodyFont, template.getBodyFontSizeSmall().intValue()) - leftPadding,
						5 * topPadding);
				stream.setFont(bodyFont, template.getBodyFontSizeSmall().floatValue());
				stream.showText(salutation);
				stream.newLine();
				stream.endText();

				stream.beginText();
				stream.newLineAtOffset(
						pageWidth - getTextWidth(salutation, bodyFont, template.getBodyFontSizeSmall().intValue()),
						3 * topPadding);
				stream.setFont(bodyFontBold, template.getBodyFontSizeSmall().floatValue());
				stream.showText(author);
				stream.newLine();
				stream.endText();

			}

			setInformation(document);
			if (template.getPasswordProtected().booleanValue()) {
				setPassword(document, currentUser, template.getPasswordLength().intValue());
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			document.save(baos);
			return baos.toByteArray();

		} catch (Exception e) {
			log.error("After Market PDF not generated");
			throw new ResourceGenerationFailedException(e.getMessage());
		}
	}

	private void setInformation(PDDocument document) {
		PDDocumentInformation information = document.getDocumentInformation();
		information.setAuthor(author);
		information.setCreator(creator);
		information.setCreationDate(Calendar.getInstance());
		information.setSubject(subject);
		information.setTitle(title);
	}

	private void setPassword(PDDocument document, User currentUser, int length) {
		try {
			AccessPermission permissions = new AccessPermission();
			permissions.setReadOnly();
			StandardProtectionPolicy policy = new StandardProtectionPolicy(currentUser.getPancardNumber(),
					currentUser.getPancardNumber(), permissions);
			policy.setEncryptionKeyLength(length);
			document.protect(policy);
		} catch (IOException e) {
			log.error("Password not set");
			throw new ResourceGenerationFailedException(e.getMessage());
		}
	}

	private float getTextWidth(String text, PDType1Font font, Integer fontSize) {
		try {
			return font.getStringWidth(text) / 1000 * fontSize;
		} catch (IOException e) {
			log.error("Text width not calculated");
			throw new ResourceGenerationFailedException(e.getMessage());
		}
	}

}
