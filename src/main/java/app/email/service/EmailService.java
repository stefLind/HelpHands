package app.email.service;

import app.email.client.EmailClient;
import app.email.client.dto.ContactDetailsRequest;
import app.email.client.dto.ContactDetailsResponse;
import app.email.client.dto.EmailNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class EmailService {
    private final EmailClient emailClient;

    @Autowired
    public EmailService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public void sendEmail(UUID userId, String emailSubject, String emailBody) {
        EmailNotificationRequest emailRequest = EmailNotificationRequest.builder()
                .userId(userId)
                .subject(emailSubject)
                .body(emailBody)
                .build();

        ResponseEntity<Void> response;
        try {
            response = emailClient.sendMail(emailRequest);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Feign call to HelpHands-Mail failed! Can't send email to user with id: [%s]!".formatted(userId));
            }
        } catch (Exception e) {
            log.warn("Feign call to HelpHands-Mail failed! Can't send email to user with id: [%s] due to 500 Internal Server Error!".formatted(userId));
        }
    }

    public void sendEmailToUsers(Set<UUID> userIds, String emailSubject, String emailBody) {
        for (UUID userId : userIds) {
            sendEmail(userId, emailSubject, emailBody);
        }
    }


    public void modifyUserContactDetails(UUID userId, String email) {
        ContactDetailsRequest contactDetailsRequest = ContactDetailsRequest.builder()
                .userId(userId)
                .contactInfo(email)
                .build();

        ResponseEntity<Void> response = emailClient.modifyUserContactDetails(contactDetailsRequest);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Feign call to HelpHands-Mail failed! Can't save contact details for user with id: [%s]!".formatted(userId));
        }
    }

    public ContactDetailsResponse getUserContactDetails(UUID userId) {
        ResponseEntity<ContactDetailsResponse> response = emailClient.getUserContactDetails(userId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Notification preference for user id [%s] does not exist!".formatted(userId));
        }

        return response.getBody();
    }
}
