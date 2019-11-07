package pl.poul12.matchzone.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poul12.matchzone.exception.ResourceNotFoundException;
import pl.poul12.matchzone.repository.MailDataRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailSender
{
    private MailDataRepository mailRepository;

    @Autowired
    public MailSender(MailDataRepository mailRepository)
    {
        this.mailRepository = mailRepository;
    }

    public final static String MESSAGE = "Success";
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    public String sendEmail(String receiverAddress, String newPassword) throws MessagingException, ResourceNotFoundException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "t.pl");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "t.pl");

        String addressName = mailRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Mail data not found")).getAddressName();
        String password = mailRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Mail data not found")).getPassword();

        Session session = Session.getInstance(properties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(addressName, password);
                    }
                });

        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(addressName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverAddress));
            message.setSubject("Password reset");
            message.setContent(getContentInHtml(newPassword), "text/html; charset=utf-8");

            Transport.send(message);

            logger.info("Mail sent to {}", receiverAddress);
            return MESSAGE;

        } catch(MessagingException e)
        {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new MessagingException();
        }
    }

    private String getContentInHtml(String newPassword){
        StringBuilder sb = new StringBuilder();
        sb.append("<p>You were requested for reset your password.</p>")
          .append("<p>Your new password is:</p>")
          .append(newPassword)
          .append("</p>")
          .append("<hr>")
          .append("<p>Sincerely, team from the match-zone.</p>");

        return sb.toString();
    }
}
