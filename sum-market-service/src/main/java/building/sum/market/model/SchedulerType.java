package building.sum.market.model;

public enum SchedulerType {

	DAILY_AFTER_MARKET("Daily After Market Update");

	private final String fileName;

	SchedulerType(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

}