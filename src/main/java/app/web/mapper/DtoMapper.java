package app.web.mapper;

import app.user.model.User;
import app.web.dto.UserEditRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserEditRequest mapUserToUserEditRequest(User user) {
        return UserEditRequest.builder()
                .firstName(user.getFirstName())
                .surname(user.getSurname())
                .lastName(user.getLastName())
                .town(user.getTown())
                .address(user.getAddress())
                .email(user.getEmail())
                .type(user.getType())
                .companyId(user.getCompany() != null ? user.getCompany().getId() : null)
                .build();
    }
}
