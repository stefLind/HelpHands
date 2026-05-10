package app.email.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ContactDetailsRequest {

    private UUID userId;

    private String contactInfo;
}
