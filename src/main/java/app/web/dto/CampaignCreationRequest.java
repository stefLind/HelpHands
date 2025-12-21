package app.web.dto;

import app.campaign.model.CampaignStatus;
import app.campaign.model.CampaignType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampaignCreationRequest {

    @Size(max = 40, message = "Title can't have more than 40 symbols.")
    private String title;

    @Size(max = 1000, message = "The description can't have more than 1000 symbols.")
    private String description;

    @Size(max = 250, message = "The short description can't have more than 250 symbols.")
    private String shortDescription;

    @Size(max = 20, message = "The location can't have more than 20 symbols.")
    private String location;

    @Size(max = 30, message = "The address can't have more than 30 symbols.")
    private String address;

    private CampaignStatus status;

    private CampaignType type;

    private LocalTime startTime;

    private LocalDate startDate;

    private LocalTime endTime;

    private LocalDate endDate;

    private Integer peopleNeeded;

    @Size(max = 30, message = "The description of the food cannot have more than 30 symbols.")
    private String foodNeeded;

    @Size(max = 30, message = "The description of the needed things cannot have more than 30 symbols.")
    private String thingsNeeded;

}
