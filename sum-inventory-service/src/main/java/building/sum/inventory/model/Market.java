package building.sum.inventory.model;

public enum Market {

	BSE(".BO"), NSE(".NS");

	private final String extension;

	Market(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return this.extension;
	}

}
