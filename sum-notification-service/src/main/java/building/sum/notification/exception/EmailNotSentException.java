package building.sum.notification.exception;

public class EmailNotSentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailNotSentException(String message) {
		super(message);
	}

}
