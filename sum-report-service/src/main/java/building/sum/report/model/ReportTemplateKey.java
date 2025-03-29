package building.sum.report.model;

public enum ReportTemplateKey {

	DAILY_AFTER_MARKET("DAILY_AFTER_MARKET_MASTER_RECORD_KEY");

	private final String primaryKey;

	ReportTemplateKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getPrimaryKey() {
		return this.primaryKey;
	}

}
