package building.sum.inventory.exception;

public class ResourceNotPostedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotPostedException(String message) {
		super(message);
	}

}
