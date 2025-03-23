package building.sum.inventory.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import building.sum.inventory.model.TableLastUpdateDetails;
import building.sum.inventory.service.CommonService;
import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/common")
public class CommonController {

	private static final Logger log = LogManager.getLogger();

	private final CommonService commonService;

	@PutMapping("/table-last-update")
	public ResponseEntity<TableLastUpdateDetails> putTableLastUpdateDetails(@RequestParam String value,
			@RequestParam String column) {
		log.info("Request received to put table update details for column - {} with value - {}", column, value);
		return new ResponseEntity<>(commonService.putTableLastUpdateDetails(column, value), HttpStatus.OK);
	}

}
