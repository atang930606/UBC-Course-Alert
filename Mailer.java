import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class Mailer{
 
	public static void sendMail2(Course course) {
 
		final String username = "ubccoursenotif@gmail.com";
		final String password = "btang930606";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		
		try {
			Message message = new MimeMessage(session);
			// send email from
			message.setFrom(new InternetAddress("ubccoursenotif@gmail.com"));
			//send email to
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(course.email));
			// email subject
			message.setSubject("UBC Course Notification");
			// email text
			message.setText("Hi,"
				+ "\n\n Your course" +course.dept+ " " + course.course + " " + 
					course.section + " is open!");
 
			Transport.send(message);
 
			System.out.println("Sent Notification Email To: " + course.email);
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}