package building.sum.notification.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import building.sum.notification.service.EmailService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/email-notify")
public class EmailController {

	private static final Logger log = LogManager.getLogger();

	private final EmailService emailService;

	@PostMapping("/with-pdf-attachment/{userJoinKey}")
	@ResponseStatus(code = HttpStatus.OK)
	public void withPdfAttachment(@RequestBody byte[] attachment, @PathVariable String userJoinKey) {
		log.info("Request received to send email for user - {}", userJoinKey);
		emailService.sendWithPdfAttachment(attachment, userJoinKey);
	}

}
