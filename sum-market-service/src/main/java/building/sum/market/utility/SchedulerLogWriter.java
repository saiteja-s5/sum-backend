package building.sum.market.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import building.sum.market.model.SchedulerType;

@Component
public class SchedulerLogWriter {

	@Value("${scheduler.log-path}")
	private String logPath;

	private static final Logger log = LogManager.getLogger();

	private final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

	private final DateTimeFormatter formatterWithDateTime = DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm:ss.SSS a");

	public void writeLog(String content, SchedulerType type) {
		try {
			String fileLocationAndName = type.getFileName() + "-" + LocalDate.now().format(formatterDate) + ".log";
			File directory = new File(logPath);
			if (!directory.exists()) {
				directory.mkdir();
			}
			File file = new File(directory.toString() + File.separator + fileLocationAndName);
			if (!file.exists()) {
				boolean created = file.createNewFile();
				if (created) {
					log.info("File not found, hence new file created");
				} else {
					log.info("File already exists");
				}
			}
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
				writer.write(LocalDateTime.now().format(formatterWithDateTime) + " : ["
						+ Thread.currentThread().getName() + "] : " + content);
				writer.newLine();
			}
		} catch (IOException ioe) {
			log.warn("Log info not written to file, Exception occured - {}", ioe.getMessage());
		}
	}

}