package building.sum.report.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import building.sum.report.service.PDFReportService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SumReportServiceListener {

	private final PDFReportService pdfReportService;

	@RabbitListener(queues = { "${daily-after-market-queue}" })
	public void generateReport(String userJoinKey) {
		pdfReportService.generateAfterMarketPdfReport(userJoinKey);
	}

}
