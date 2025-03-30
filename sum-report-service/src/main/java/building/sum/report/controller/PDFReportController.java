package building.sum.report.controller;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import building.sum.report.service.PDFReportService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/pdf-report")
public class PDFReportController {

	private static final Logger log = LogManager.getLogger();

	private final PDFReportService pdfReportService;

	@GetMapping("/daily-after-market/{userJoinKey}")
	public ResponseEntity<byte[]> generateAfterMarketReport(@PathVariable String userJoinKey) {
		log.info("Request received to generate after market pdf report on {} for user - {}", LocalDateTime.now(),
				userJoinKey);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		return new ResponseEntity<>(pdfReportService.generateAfterMarketPdfReport(userJoinKey), headers, HttpStatus.OK);
	}

}
