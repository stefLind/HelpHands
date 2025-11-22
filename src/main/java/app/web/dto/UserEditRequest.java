package app.web.dto;

import app.user.model.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserEditRequest {

    @Size(max = 20, message = "First name can't have more than 20 symbols.")
    private String firstName;

    @Size(max = 20, message = "Surname can't have more than 20 symbols.")
    private String surname;

    @Size(max = 20, message = "Last name can't have more than 20 symbols.")
    private String lastName;

    @Size(max = 50, message = "Company name can't have more than 50 symbols.")
    private String companyName;

    @Email(message = "Requires correct email format.")
    private String email;

    @Size(max = 20, message = "Town name can't have more than 20 symbols.")
    private String town;

    @Size(max = 30, message = "The address can't have more than 30 symbols.")
    private String address;

    private UserType type;

    private UUID companyId;
}
