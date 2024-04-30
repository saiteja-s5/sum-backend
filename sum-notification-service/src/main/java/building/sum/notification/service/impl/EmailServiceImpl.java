package building.sum.notification.service.impl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import building.sum.notification.exception.EmailNotSentException;
import building.sum.notification.exception.ResourceNotFoundException;
import building.sum.notification.model.EmailConfiguration;
import building.sum.notification.model.EmailSentInfo;
import building.sum.notification.model.User;
import building.sum.notification.service.EmailService;
import building.sum.notification.service.repository.EmailConfigurationRepository;
import building.sum.notification.service.repository.EmailSentInfoRepository;
import building.sum.notification.service.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LogManager.getLogger();

	private static final String PDF_MIME_TYPE = "application/pdf";

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

	@Value("${email.daily-market-email-code}")
	private String emailCode;

	@Value("${email.self-email-reference}")
	private String emailSelfReferencePhrase;

	private JavaMailSender mailSender;

	private TemplateEngine templateEngine;

	private UserRepository userRepository;

	private EmailSentInfoRepository emailSentInfoRepository;

	private EmailConfigurationRepository emailConfigurationRepository;

	public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, UserRepository userRepository,
			EmailSentInfoRepository emailSentInfoRepository,
			EmailConfigurationRepository emailConfigurationRepository) {
		super();
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
		this.userRepository = userRepository;
		this.emailSentInfoRepository = emailSentInfoRepository;
		this.emailConfigurationRepository = emailConfigurationRepository;
	}

	@Override
	public void sendWithPdfAttachment(byte[] attachment, String userJoinKey) {

		// Email Sent Details
		EmailSentInfo sentInfo = new EmailSentInfo();
		sentInfo.setCreatedDateTime(LocalDateTime.now());
		sentInfo.setIsActive(1);

		try {
			// User Details Fetch
			Optional<User> currentUserContainer = userRepository.findByUserJoinKeyIgnoreCase(userJoinKey);
			User currentUser;
			if (!currentUserContainer.isPresent()) {
				throw new ResourceNotFoundException(String.format("User with Join Key - %s not found", userJoinKey));
			} else {
				currentUser = currentUserContainer.get();
			}

			sentInfo.setCreatedBy(currentUser.getUserJoinKey());

			// Email Details Fetch
			Optional<EmailConfiguration> emailConfigurationContainer = emailConfigurationRepository
					.findByEmailCodeIgnoreCase(emailCode);
			EmailConfiguration emailConfiguration;
			if (emailConfigurationContainer.isPresent() && emailConfigurationContainer.get().getIsActive() == 1) {

				emailConfiguration = emailConfigurationContainer.get();

				sentInfo.setEmailKey(emailConfiguration.getEmailKey().intValue());

				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

				boolean valid = true;

				// Set From
				if (validString(emailConfiguration.getEmailFrom())) {
					messageHelper.setFrom(emailConfiguration.getEmailFrom());
					sentInfo.setEmailSentFrom(emailConfiguration.getEmailFrom());
				} else {
					log.warn("From - {} is not valid", emailConfiguration.getEmailFrom());
					valid = false;
				}

				// Set To
				if (validString(emailConfiguration.getEmailTo())) {
					if (emailConfiguration.getEmailTo().contains(";")) {
						List<String> toEmails = Arrays.asList(emailConfiguration.getEmailTo().split(";")).stream()
								.filter(this::validString).map(email -> email.trim().toLowerCase()).distinct().toList();
						for (String email : toEmails) {
							if (email.equalsIgnoreCase(emailSelfReferencePhrase)) {
								toEmails.add(currentUser.getEmailId());
							}
						}
						toEmails = toEmails.stream().filter(email -> !email.equalsIgnoreCase(emailSelfReferencePhrase))
								.toList();
						if (!toEmails.isEmpty()) {
							sentInfo.setEmailSentTo(toEmails.toString());
							messageHelper.setTo(toEmails.toArray(new String[0]));
						} else {
							log.error("To list is empty");
							valid = false;
						}
					} else {
						sentInfo.setEmailSentTo(emailConfiguration.getEmailTo());
						messageHelper.setTo(emailConfiguration.getEmailTo());
					}
				} else {
					log.warn("To - {} is not valid", emailConfiguration.getEmailTo());
					valid = false;
				}

				// Set Cc
				if (validString(emailConfiguration.getEmailCc())) {
					if (emailConfiguration.getEmailCc().contains(";")) {
						List<String> ccEmails = Arrays.asList(emailConfiguration.getEmailCc().split(";")).stream()
								.filter(this::validString).map(email -> email.trim().toLowerCase()).distinct().toList();
						for (String email : ccEmails) {
							if (email.equalsIgnoreCase(emailSelfReferencePhrase)) {
								ccEmails.add(currentUser.getEmailId());
							}
						}
						ccEmails = ccEmails.stream().filter(email -> !email.equalsIgnoreCase(emailSelfReferencePhrase))
								.toList();
						if (!ccEmails.isEmpty()) {
							messageHelper.setCc(ccEmails.toArray(new String[0]));
						} else {
							log.error("Cc list is empty");
						}
					} else {
						messageHelper.setCc(emailConfiguration.getEmailCc());
					}
				} else {
					log.warn("Cc - {} is not valid", emailConfiguration.getEmailCc());
				}

				// Set Bcc
				if (validString(emailConfiguration.getEmailBcc())) {
					if (emailConfiguration.getEmailBcc().contains(";")) {
						List<String> bccEmails = Arrays.asList(emailConfiguration.getEmailBcc().split(";")).stream()
								.filter(this::validString).map(email -> email.trim().toLowerCase()).distinct().toList();
						for (String email : bccEmails) {
							if (email.equalsIgnoreCase(emailSelfReferencePhrase)) {
								bccEmails.add(currentUser.getEmailId());
							}
						}
						bccEmails = bccEmails.stream().filter(email -> !email.equalsIgnoreCase(emailSelfReferencePhrase))
								.toList();
						if (!bccEmails.isEmpty()) {
							messageHelper.setBcc(bccEmails.toArray(new String[0]));
						} else {
							log.error("Bcc list is empty");
						}
					} else {
						messageHelper.setBcc(emailConfiguration.getEmailBcc());
					}
				} else {
					log.warn("Bcc - {} is not valid", emailConfiguration.getEmailBcc());
				}

				// Set Subject
				if (validString(emailConfiguration.getEmailSubject())) {
					messageHelper.setSubject(emailConfiguration.getEmailSubject());
				} else {
					log.warn("Subject - {} is not valid", emailConfiguration.getEmailSubject());
				}

				// Set Body
				// TODO Body
				Context context = new Context();
				context.setVariable("user-name", currentUser.getFirstName());
				String bodyHtml = templateEngine.process(emailConfiguration.getEmailBodyTemplateName(), context);
				messageHelper.setText(bodyHtml, true);

				// Set Attachment
				ByteArrayDataSource dataSource = new ByteArrayDataSource(attachment, PDF_MIME_TYPE);
				String attachmentFileName = "Statement-" + LocalDate.now().format(dateFormatter) + ".pdf";
				messageHelper.addAttachment(attachmentFileName, dataSource);

				if (valid) {
					mailSender.send(message);
					sentInfo.setEmailSentStatus(1);
					sentInfo.setEmailSentDateTime(LocalDateTime.now());
					log.info("Email sent sucessfully");
				} else {
					sentInfo.setEmailSentStatus(0);
					log.warn("Email not sent, please check the attributes");
				}

			} else {
				throw new EmailNotSentException("Email configuration is not in order");
			}

		} catch (Exception e) {
			log.error("Email not sent");
			sentInfo.setEmailSentStatus(0);
			throw new EmailNotSentException(e.getMessage());

		} finally {
			emailSentInfoRepository.save(sentInfo);
		}
	}

	private boolean validString(String toBeChecked) {
		return toBeChecked != null && toBeChecked.trim().length() > 0;
	}

}