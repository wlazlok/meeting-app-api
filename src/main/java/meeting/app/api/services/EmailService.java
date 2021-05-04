package meeting.app.api.services;

import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.user.UserEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

@EnableAsync
@Service
public class EmailService {

    private final String host = "smtp.gmail.com";
    private final String from = "";
    private final String password = "";

    @Async
    public void sendActivateEmail(UserEntity user, String path) {
        Session session = setUp();

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[1];
            toAddress[0] = new InternetAddress(user.getEmail());
            message.addRecipient(Message.RecipientType.TO, toAddress[0]);

            message.setSubject("Aktywacja konta");
            message.setText("Kliknij w poniższy link aby aktywować konto: \n" + generateActivateLink(user.getActivateAccountUUID().toString(), path, String.valueOf(user.getId())));

            Transport transport = session.getTransport("smtp");

            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException | MalformedURLException ae) {
            ae.printStackTrace();
        }
    }

    @Async
    public void sendResetEmail(UserEntity user, String path) {
        Session session = setUp();

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[1];
            toAddress[0] = new InternetAddress(user.getEmail());
            message.addRecipient(Message.RecipientType.TO, toAddress[0]);

            message.setSubject("Resetowanei hasła");
            message.setText("Kliknij w poniższy link aby zresetować hasło: \n" + generateResetPasswordLink(user.getResetPasswordUUID().toString(), path, String.valueOf(user.getId())));

            Transport transport = session.getTransport("smtp");

            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException | MalformedURLException ae) {
            ae.printStackTrace();
        }
    }

    @Async
    public void sendEventNotification(String[] to, EventItem eventItem) {
        Session session = setUp();

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }
            for (InternetAddress address : toAddress) {
                message.addRecipient(Message.RecipientType.TO, address);
            }

            message.setSubject("Nadchodzące wydarzenia");
            message.setText("Twoje zbliżające się wydarzenia: \n" + eventItem.getDescription() + " - " +  eventItem.getDate() + " - "
                    + eventItem.getCity() + " " + eventItem.getStreet());

            Transport transport = session.getTransport("smtp");

            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException ae) {
            ae.printStackTrace();
        }
    }

    public URL generateActivateLink(String uuid, String path, String userId) throws MalformedURLException {
        return new URL("http://localhost:8080/api/user/activate?id=" + uuid + "&usr=" + userId);
    }

    public URL generateResetPasswordLink(String uuid, String path, String userId) throws MalformedURLException {
        return new URL("http://localhost:8080/api/user/change?id=" + uuid + "&usr=" + userId);
    }

    private Session setUp() {
        Properties props = System.getProperties();

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        return Session.getDefaultInstance(props);
    }
}
