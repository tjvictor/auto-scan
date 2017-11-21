package autoscan.dao.imp;

import autoscan.model.Email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;

@Component
public class EmailSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendEmail(Email email) {
        requireNonNull(email, "email cannot be null");
        requireNonNull(email.getTo(), "email To address cannot be null");
        this.logger.info("Starting to Send Email...");
        MimeMessage emailMessage = this.javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true);
            helper.setTo(email.getTo());
            helper.setReplyTo(email.getReplyTo(), email.getReplyToDisplay());
            helper.setFrom(email.getFrom(), email.getFromDisplay());

            helper.setSubject(email.getSubject());
            helper.setText(email.getContent(), email.isHtml());
            this.javaMailSender.send(emailMessage);
            this.logger.info("Email send successfully to : {}, subject : {}", email.getTo(), email.getSubject());
            return true;
        } catch (Exception var4) {
            this.logger.info("Failed to send email.", var4);
            return false;
        }
    }

    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        } else {
            return obj;
        }
    }
}
