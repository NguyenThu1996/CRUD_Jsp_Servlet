package tool;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	public static void senMailLogin(String subject, String body) throws IOException {
		String EMAIL_FROM = "nguyenvanthu.ptp@gmail.com";
		String PASSWORD = "phuongthao1998";
		String EMAIL_TO = "nguyenvanthu.itbk@gmail.com";

		Properties prop = new Properties();		
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		prop.setProperty("mail.smtp.host","smtp.gmail.com"); 
		prop.setProperty("mail.smtp.port", "587");
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL_FROM, PASSWORD);
			}
		};
		Session session = Session.getInstance(prop, auth);
		try {
			Message msg = new MimeMessage(session);
			// from
			msg.setFrom(new InternetAddress(EMAIL_FROM));
			// to
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(EMAIL_TO));
			// subject
			msg.setSubject(subject);

			// content
			msg.setText(body);
			msg.saveChanges();

			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}
}
