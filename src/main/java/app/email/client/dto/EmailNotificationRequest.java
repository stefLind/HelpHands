package app.email.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EmailNotificationRequest {

    UUID userId;

    private String subject;

    private String body;
}
