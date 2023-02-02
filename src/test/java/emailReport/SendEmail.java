package emailReport;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/*
 * This class has the main code for sending mail
 */
public class SendEmail {
	@SuppressWarnings("unused")
	public static void send(String from, String tos[], String ccs[], String subject, String text)
			throws MessagingException {
		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("karna@neokred.tech", "NKtech@1234");// change accordingly
			}
		});
		// compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("karna@neokred.tech"));// change accordingly
			for (String to : tos) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress("chandana@neokred.tech"));
			}
			for (String cc : ccs) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress("support@neokred.tech"));
			}
			message.setSubject(subject);
			// Option 1: To send normal text message
			// message.setText(text);
			// Option 2: Send the actual HTML message, as big as you like
			// message.setContent("<h1>This is actual message</h1></br></hr>" +
			// text, "text/html");

			String repName = "C:\\Users\\Karna\\Documents\\Book4.xlsx";
			// Set the attachment path
			String filename = System.getProperty("C:\\Users\\Karna\\Documents\\Book4.xlsx");
			BodyPart objMessageBodyPart = new MimeBodyPart();
			// Option 3: Send text along with attachment
			objMessageBodyPart.setContent("<h3>Dear Team,</h3></br>" + text, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(objMessageBodyPart);
			objMessageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(repName);
			objMessageBodyPart.setDataHandler(new DataHandler(source));
			objMessageBodyPart.setFileName(repName);
			multipart.addBodyPart(objMessageBodyPart);
			message.setContent(multipart);
			// send message
			Transport.send(message);
			System.out.println("message sent successfully");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}// End of SEND method

	public static void main(String[] args) throws MessagingException {

		String to[] = { "chandana@neokred.tech" };
		String cc[] = { "" };
		SendEmail.send("karna@neokred.tech", to, cc, "Aspire Load Reversal",
				"Please find the load reversal transactions");
	}
}
