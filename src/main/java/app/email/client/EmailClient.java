package app.email.client;

import app.email.client.dto.ContactDetailsRequest;
import app.email.client.dto.ContactDetailsResponse;
import app.email.client.dto.EmailNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "HelpHands-Mail", url = "http://localhost:8081/api/v1/email-notifications")
public interface EmailClient {

    @PostMapping
    ResponseEntity<Void> sendMail(@RequestBody EmailNotificationRequest notificationRequest);

    @PostMapping("/contact-details")
    ResponseEntity<Void> modifyUserContactDetails(@RequestBody ContactDetailsRequest contactDetailsRequest);

    @GetMapping("/contact-details")
    ResponseEntity<ContactDetailsResponse> getUserContactDetails(@RequestParam(name = "userId") UUID userId);
}
