package building.sum.report.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import building.sum.report.dto.StockDashboardDTO;
import building.sum.report.exception.ResourceGenerationFailedException;
import building.sum.report.service.PdfReportService;

@Service
public class PdfReportServiceImpl implements PdfReportService {

	private static final Logger log = LogManager.getLogger();

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, EEEE");

	private final DateTimeFormatter dateTimeFormatterWithTime = DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mm a");

	@Autowired
	private ResourceLoader loader;

	@Autowired
	private WebClient.Builder webClientBuilder;

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
	public byte[] generateAfterMarketPdfReport() {
		try (PDDocument document = new PDDocument()) {

			PDPage page = new PDPage();
			document.addPage(page);

			float pageWidth = page.getMediaBox().getWidth();
			float pageHeight = page.getMediaBox().getHeight();
			int leftPadding = 20;
			int topPadding = 10;

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
				// TODO User Details from DB
				stream.newLineAtOffset(leftPadding, pageHeight - sumIcon.getHeight() - topPadding - 2 * topPadding);

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Name");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText(creator);
				stream.newLine();

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Phone");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("9505166056");
				stream.newLine();

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Email");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("saithejasabbani1@gmail.com");
				stream.newLine();

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Pancard");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText("BWTPT4086P");
				stream.newLine();

				stream.setFont(bodyFontBold, bodyFontSize);
				stream.showText("Trade Date");
				stream.showText(" : ");
				stream.setFont(bodyFont, bodyFontSize);
				stream.showText(LocalDate.now().format(dateTimeFormatter));
				stream.newLine();
				stream.endText();

				// Table Current Holdings
				// TODO Table
				StockDashboardDTO holdings = webClientBuilder.build().get()
						.uri("http://sum-market-service/daily-market/dashboard").retrieve()
						.bodyToMono(StockDashboardDTO.class).block();
				stream.beginText();
//				stream.newLineAtOffset(leftPadding, 5 * topPadding);
//				stream.showText(holdings.toString());
				stream.endText();

				// Table Summary

				// Signature
				stream.beginText();
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
				stream.showText("Hyderabad");
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
			setPassword(document);

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

	private void setPassword(PDDocument document) {
		// TODO set passwords from dB
		try {
			AccessPermission permissions = new AccessPermission();
			permissions.setReadOnly();
			StandardProtectionPolicy policy = new StandardProtectionPolicy("1234", "1111", permissions);
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
