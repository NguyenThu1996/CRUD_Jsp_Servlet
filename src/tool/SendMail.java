package tool;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	public static void senMailLogin(String subject, String body) throws IOException {
		 //InputStream configStream = c.getRequest().getServletContext().getResourceAsStream("/WEB-INF/emailconfig.xml");
		  
		  String result;
		  // Recipient's email ID needs to be mentioned.
		  String to = "nguyenvanthu.itbk@gmail.com";
		  
		  // Sender's email ID needs to be mentioned
		  final String from = "nguyenvanthu.ptp@gmail.com";
			
		  final String emailPassword = "phuongthao1998";

		  // Host of mail server
		  //String host = "smtp.gmail.com";
		  String host = "smtp.gmail.com";
			
		  // Port of mail server
		  String port = "587";

		  // Get system properties object
		  Properties properties = System.getProperties();

		  // Setup mail server
		  properties.setProperty("mail.smtp.auth", "true");
		  properties.setProperty("mail.smtp.starttls.enable", "true");
		  properties.setProperty("mail.smtp.host", host);
		  properties.setProperty("mail.smtp.port", port);

		  // Get the default Session object.
		  //Session mailSession = Session.getDefaultInstance(properties);
			
		  Session mailSession = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() { 
				return new PasswordAuthentication(from, emailPassword); 
				} 
		  });

		  try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(mailSession);
			    
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			    
			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO,
			                               new InternetAddress(to));
			// Set Subject: header field
			message.setSubject("thumbswriパスワード通知");
			
			// Now set the actual message
			message.setText("パスワードを発行しました。");
			
			// Send message
			Transport.send(message);
			result = "Sent message successfully....";
			System.out.println(result);
		
		 }catch (MessagingException mex) {
			mex.printStackTrace();
			result = "Error: unable to send message....";
			System.out.println(mex.getMessage());
		}

	}
}
