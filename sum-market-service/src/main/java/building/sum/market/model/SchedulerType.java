package building.sum.market.model;

public enum SchedulerType {

	DAILY_AFTER_MARKET_TABLES_UPDATER("Daily After Market Table Update"),
	DAILY_AFTER_MARKET_REPORT("Daily After Market Report");

	private final String fileName;

	SchedulerType(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

}