package building.sum.report.service.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import building.sum.report.dto.StockDashboardDTO;
import building.sum.report.exception.ResourceGenerationFailedException;
import building.sum.report.exception.ResourceNotFoundException;
import building.sum.report.model.AppDetails;
import building.sum.report.model.User;
import building.sum.report.service.PdfReportService;
import building.sum.report.service.repository.AppDetailsRepository;
import building.sum.report.service.repository.UserRepository;
import building.sum.report.service.utility.SumUtility;

@Service
public class PdfReportServiceImpl implements PdfReportService {

	private static final Logger log = LogManager.getLogger();

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, EEEE");

	private final DateTimeFormatter dateTimeFormatterWithTime = DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm a");

	private ResourceLoader loader;

	private WebClient.Builder webClientBuilder;

	private UserRepository userRepository;

	private AppDetailsRepository appDetailsRepository;

	public PdfReportServiceImpl(ResourceLoader loader, Builder webClientBuilder, UserRepository userRepository,
			AppDetailsRepository appDetailsRepository) {
		super();
		this.loader = loader;
		this.webClientBuilder = webClientBuilder;
		this.userRepository = userRepository;
		this.appDetailsRepository = appDetailsRepository;
	}

	@Value("${pdf.author-name}")
	private String author;

	@Value("${pdf.creator}")
	private String creator;

	@Value("${pdf.subject}")
	private String subject;

	@Value("${pdf.title}")
	private String title;

	@Value("${pdf.passowrd.encryption-key-length}")
	private Integer excryptionKeyLength;

	@Value("${pdf.logo-file-name}")
	private String imageName;

	@Value("${pdf.heading}")
	private String heading;

	@Value("${pdf.header-font-size}")
	private Integer headerFontSize;

	@Value("${pdf.body-font-size}")
	private Integer bodyFontSize;

	@Value("${pdf.paragraph-line-spacing}")
	private Integer lineSpacing;

	@Value("${pdf.paragraph-line-spacing-small}")
	private Integer lineSpacingSmall;

	@Value("${pdf.body-font-small-size}")
	private Integer bodySmallFontSize;

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

		Optional<AppDetails> detailsContainer = appDetailsRepository.findById(SumUtility.TABLE_APP_DETAILS_PK);
		AppDetails details;
		if (!detailsContainer.isPresent()) {
			throw new ResourceNotFoundException(
					String.format("App Details with Key - %s not found", SumUtility.TABLE_APP_DETAILS_PK));
		} else {
			details = detailsContainer.get();
		}

		try (PDDocument document = new PDDocument()) {

			PDPage page = new PDPage();
			document.addPage(page);

			float pageWidth = page.getMediaBox().getWidth();
			float pageHeight = page.getMediaBox().getHeight();
			float leftPadding = 20;
			float topPadding = 10;

			try (PDPageContentStream stream = new PDPageContentStream(document, page)) {

				PDType1Font headerFont = new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD);
				PDType1Font bodyFont = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
				PDType1Font bodyFontBold = new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD);

				// Set SUM Logo
				URI imageUrl = loader.getResource("classpath:" + File.separator + "images" + File.separator + imageName)
						.getURI();
				PDImageXObject sumIcon = PDImageXObject.createFromFile(imageUrl.getPath(), document);
				stream.drawImage(sumIcon, leftPadding / 2, pageHeight - sumIcon.getHeight() - topPadding);

				// Set Heading
				stream.beginText();
				stream.setFont(headerFont, headerFontSize);
				stream.newLineAtOffset(pageWidth / 2 - getTextWidth(heading, headerFont, headerFontSize) / 2,
						pageHeight - topPadding * 6);
				stream.showText(heading);
				stream.endText();

				// Set User Details
				stream.beginText();
				stream.setLeading(lineSpacing);
				stream.newLineAtOffset(leftPadding, pageHeight - sumIcon.getHeight() - topPadding - 2 * topPadding);

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Name");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText(currentUser.getFirstName());
				stream.newLine();

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Phone");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText(currentUser.getContactNumber());
				stream.newLine();

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Email");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText(currentUser.getEmailId());
				stream.newLine();

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Pancard");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText(currentUser.getPancardNumber());
				stream.newLine();

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Trade Date");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText(LocalDate.now().format(dateTimeFormatter));
				stream.newLine();
				stream.endText();

				// Table Current Holdings

				// Stock Investment Breakdown
				StockDashboardDTO holdings = webClientBuilder.build().get()
						.uri("http://localhost:9595/daily-market/" + userJoinKey + "/dashboard").retrieve()
						.bodyToMono(StockDashboardDTO.class).block();

				if (holdings != null) {

					stream.beginText();
					String stockHeading = "Stock Investment Breakdown";
					stream.newLineAtOffset(leftPadding,
							pageHeight - sumIcon.getHeight() - topPadding - 17 * topPadding);
					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFontBold, bodyFontSize * 1.1f);
					stream.showText(stockHeading);
					stream.endText();

					stream.beginText();
					stream.newLineAtOffset(leftPadding,
							pageHeight - sumIcon.getHeight() - topPadding - 20 * topPadding);

					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFont, bodyFontSize);
					stream.showText("Total Investment");
					stream.showText(" : ");
					stream.setNonStrokingColor(Color.BLUE);
					stream.setFont(bodyFont, bodyFontSize);
					stream.showText(Optional.ofNullable(holdings.getTotalStockInvestmentValue()).isPresent()
							? holdings.getTotalStockInvestmentValue().toString()
							: "0");
					stream.newLine();

					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFont, bodyFontSize);
					stream.showText("Current Return");
					stream.showText(" : ");
					stream.setNonStrokingColor(
							holdings.getTotalStockCurrentReturn().doubleValue() > 0 ? Color.GREEN : Color.RED);
					stream.setFont(bodyFont, bodyFontSize);
					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentReturn()).isPresent()
							? holdings.getTotalStockCurrentReturn().toString()
							: "0");
					stream.newLine();

					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFont, bodyFontSize);
					stream.showText("Current Return %");
					stream.showText(" : ");
					stream.setNonStrokingColor(
							holdings.getTotalStockCurrentReturnPercent().doubleValue() > 0 ? Color.GREEN : Color.RED);
					stream.setFont(bodyFont, bodyFontSize);
					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentReturnPercent()).isPresent()
							? holdings.getTotalStockCurrentReturnPercent().toString()
							: "0");
					stream.newLine();

					stream.setNonStrokingColor(Color.BLACK);
					stream.setFont(bodyFont, bodyFontSize);
					stream.showText("Current Portfolio Value");
					stream.showText(" : ");
					stream.setNonStrokingColor(holdings.getTotalStockCurrentValue().doubleValue() > holdings
							.getTotalStockInvestmentValue().doubleValue() ? Color.GREEN : Color.RED);
					stream.setFont(bodyFont, bodyFontSize);
					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentValue()).isPresent()
							? holdings.getTotalStockCurrentValue().toString()
							: "0");
					stream.newLine();

					stream.endText();
				}

				// Mutual Fund Investment Breakdown
				// TODO Mutual Fund Api
//				StockDashboardDTO holdings = webClientBuilder.build().get()
//						.uri("http://localhost:9595/daily-market/" + userJoinKey + "/dashboard").retrieve()
//						.bodyToMono(StockDashboardDTO.class).block();

//				if (holdings != null) {

				stream.beginText();
				String mutualFundHeading = "Mutual Fund Investment Breakdown";
				stream.newLineAtOffset(leftPadding, pageHeight - sumIcon.getHeight() - topPadding - 32 * topPadding);
				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFontBold, bodyFontSize * 1.1f);
				stream.showText(mutualFundHeading);
				stream.endText();

				stream.beginText();
				stream.newLineAtOffset(leftPadding, pageHeight - sumIcon.getHeight() - topPadding - 35 * topPadding);

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("Total Investment");
				stream.showText(" : ");
				stream.setNonStrokingColor(Color.BLUE);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("XXX");
//					stream.showText(Optional.ofNullable(holdings.getTotalStockInvestmentValue()).isPresent()
//							? holdings.getTotalStockInvestmentValue().toString()
//							: "0");
				stream.newLine();

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("Current Return");
				stream.showText(" : ");
				stream.setNonStrokingColor(
						holdings.getTotalStockCurrentReturn().doubleValue() > 0 ? Color.GREEN : Color.RED);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("XXX");
//					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentReturn()).isPresent()
//							? holdings.getTotalStockCurrentReturn().toString()
//							: "0");
				stream.newLine();

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("Current Return %");
				stream.showText(" : ");
				stream.setNonStrokingColor(
						holdings.getTotalStockCurrentReturnPercent().doubleValue() > 0 ? Color.GREEN : Color.RED);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("XXX");
//					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentReturnPercent()).isPresent()
//							? holdings.getTotalStockCurrentReturnPercent().toString()
//							: "0");
				stream.newLine();

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("Current Portfolio Value");
				stream.showText(" : ");
				stream.setNonStrokingColor(holdings.getTotalStockCurrentValue().doubleValue() > holdings
						.getTotalStockInvestmentValue().doubleValue() ? Color.GREEN : Color.RED);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("XXX");
//					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentValue()).isPresent()
//							? holdings.getTotalStockCurrentValue().toString()
//							: "0");
				stream.newLine();

				stream.endText();
//				}

				// Table Summary

				stream.beginText();
				String summaryHeading = "Overall Investment Breakdown";
				stream.newLineAtOffset(leftPadding, pageHeight - sumIcon.getHeight() - topPadding - 47 * topPadding);
				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFontBold, bodyFontSize * 1.1f);
				stream.showText(summaryHeading);
				stream.endText();

				stream.beginText();
				stream.newLineAtOffset(leftPadding, pageHeight - sumIcon.getHeight() - topPadding - 50 * topPadding);

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("Total Investment");
				stream.showText(" : ");
				stream.setNonStrokingColor(Color.BLUE);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("XXX");
//					stream.showText(Optional.ofNullable(holdings.getTotalStockInvestmentValue()).isPresent()
//							? holdings.getTotalStockInvestmentValue().toString()
//							: "0");
				stream.newLine();

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("Current Return");
				stream.showText(" : ");
				stream.setNonStrokingColor(
						holdings.getTotalStockCurrentReturn().doubleValue() > 0 ? Color.GREEN : Color.RED);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("XXX");
//					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentReturn()).isPresent()
//							? holdings.getTotalStockCurrentReturn().toString()
//							: "0");
				stream.newLine();

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("Current Return %");
				stream.showText(" : ");
				stream.setNonStrokingColor(
						holdings.getTotalStockCurrentReturnPercent().doubleValue() > 0 ? Color.GREEN : Color.RED);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("XXX");
//					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentReturnPercent()).isPresent()
//							? holdings.getTotalStockCurrentReturnPercent().toString()
//							: "0");
				stream.newLine();

				stream.setNonStrokingColor(Color.BLACK);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("Current Portfolio Value");
				stream.showText(" : ");
				stream.setNonStrokingColor(holdings.getTotalStockCurrentValue().doubleValue() > holdings
						.getTotalStockInvestmentValue().doubleValue() ? Color.GREEN : Color.RED);
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("XXX");
//					stream.showText(Optional.ofNullable(holdings.getTotalStockCurrentValue()).isPresent()
//							? holdings.getTotalStockCurrentValue().toString()
//							: "0");
				stream.newLine();

				stream.endText();

				// Signature
				stream.beginText();
				stream.setNonStrokingColor(Color.BLACK);
				stream.setLeading(lineSpacingSmall);
				stream.newLineAtOffset(leftPadding, 5 * topPadding);
				stream.setFont(bodyFontBold, bodySmallFontSize);
				stream.showText("Date");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodySmallFontSize);
				stream.showText(LocalDateTime.now().format(dateTimeFormatterWithTime));
				stream.newLine();

				stream.setFont(bodyFontBold, bodySmallFontSize);
				stream.showText("Place");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodySmallFontSize);
				stream.showText(details.getAppAddressCity());
				stream.newLine();
				stream.endText();

				stream.beginText();
				stream.setLeading(lineSpacingSmall);
				String salutation = "Yours faithfully,";
				stream.newLineAtOffset(pageWidth - getTextWidth(salutation, bodyFont, bodySmallFontSize) - leftPadding,
						5 * topPadding);
				stream.setFont(bodyFont, bodySmallFontSize);
				stream.showText(salutation);
				stream.newLine();
				stream.endText();

				stream.beginText();
				stream.newLineAtOffset(pageWidth - getTextWidth(salutation, bodyFont, bodySmallFontSize),
						3 * topPadding);
				stream.setFont(bodyFontBold, bodySmallFontSize);
				stream.showText(author);
				stream.newLine();
				stream.endText();

			}

			setInformation(document);
			setPassword(document, currentUser, details);
			
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

	private void setPassword(PDDocument document, User currentUser, AppDetails details) {
		try {
			AccessPermission permissions = new AccessPermission();
			permissions.setReadOnly();
			StandardProtectionPolicy policy = new StandardProtectionPolicy(details.getAppReportsOwnerPassword(),
					currentUser.getPancardNumber(), permissions);
			policy.setEncryptionKeyLength(excryptionKeyLength);
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
