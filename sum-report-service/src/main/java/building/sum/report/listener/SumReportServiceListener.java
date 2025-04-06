package building.sum.report.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import building.sum.report.service.PDFReportService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SumReportServiceListener {

	private final PDFReportService pdfReportService;

	private static final Logger log = LogManager.getLogger();

	@RabbitListener(queues = { "${daily-after-market-queue}" })
	public void generateReport(String userJoinKey) {
		log.info("Received report generation request for user - {}", userJoinKey);
		pdfReportService.generateAfterMarketPdfReport(userJoinKey);
	}

}
