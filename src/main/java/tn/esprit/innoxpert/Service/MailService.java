
package tn.esprit.innoxpert.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendMeetingInvitation(String to, String subject, String meetingLink, LocalDateTime startTime) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String formattedTime = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Dear user,<br><br>Your consultation is scheduled on <b>" + formattedTime + "</b>.<br>" +
                "Here is your meeting link: <a href=\"" + meetingLink + "\">Join Meeting</a><br><br>" +
                "Best regards,<br>Consulting Team", true);

        mailSender.send(message);
    }
}
