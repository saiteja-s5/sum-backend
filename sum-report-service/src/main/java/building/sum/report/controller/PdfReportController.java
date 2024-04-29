package building.sum.report.controller;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import building.sum.report.service.PdfReportService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/pdf-reports")
public class PdfReportController {

	private static final Logger log = LogManager.getLogger();

	private final PdfReportService pdfReportService;

	@GetMapping("/after-market")
	public ResponseEntity<byte[]> generateAfterMarketReport() {
		log.info("Request received to generate after market pdf report on {}", LocalDateTime.now());
		return new ResponseEntity<>(pdfReportService.generateAfterMarketPdfReport(), HttpStatus.OK);
	}

}
