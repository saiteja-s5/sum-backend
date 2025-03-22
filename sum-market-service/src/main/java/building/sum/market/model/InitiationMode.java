package building.sum.market.model;

public enum InitiationMode {

	MANUAL("api"), SCHEDULER("daily-after-market-scheduler");

	private final String mode;

	InitiationMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return this.mode;
	}

}
