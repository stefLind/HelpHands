package app.web.dto;

import app.user.model.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 6, message = "Username should be at least 6 characters.")
    private String username;

    @Size(min = 6, message = "Password should be at least 6 characters.")
    private String password;

    @Size(min = 6, message = "Password should be at least 6 characters.")
    private String confirmedPassword;

    @Email
    private String email;

    private UserType userType;
}
