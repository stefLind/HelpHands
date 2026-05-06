package app.web.mapper;

import app.campaign.model.Campaign;
import app.user.model.User;
import app.web.dto.CampaignModificationRequest;
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
                .companyName(user.getCompanyName())
                .companyId(user.getCompany() != null ? user.getCompany().getId() : null)
                .build();
    }

    public static CampaignModificationRequest mapCampaignToCampaignModificationRequest(Campaign  campaign) {
        return CampaignModificationRequest.builder()
                .title(campaign.getTitle())
                .description(campaign.getDescription())
                .shortDescription(campaign.getShortDescription())
                .location(campaign.getLocation())
                .address(campaign.getAddress())
                .status(campaign.getStatus())
                .type(campaign.getType())
                .startDate(campaign.getStartDate())
                .endDate(campaign.getEndDate())
                .peopleNeeded(campaign.getPeopleNeeded())
                .foodNeeded(campaign.getFoodNeeded())
                .thingsNeeded(campaign.getThingsNeeded())
                .build();
    }
}
