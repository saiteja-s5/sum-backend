package building.sum.notification.service;

public interface EmailService {

	void sendWithPdfAttachment(byte[] attachment, String userJoinKey);

}
