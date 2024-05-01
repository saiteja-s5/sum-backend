package building.sum.market.model;

public enum SchedulerType {

	DAILY_MARKET("Daily Market");

	private final String fileName;

	SchedulerType(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

}