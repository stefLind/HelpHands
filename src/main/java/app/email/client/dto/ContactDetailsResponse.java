package app.email.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactDetailsResponse {

    private String contactInfo;
}
