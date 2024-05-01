package building.sum.market.exception;

public class SchedulerStoppedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SchedulerStoppedException(String message) {
		super(message);
	}

}
