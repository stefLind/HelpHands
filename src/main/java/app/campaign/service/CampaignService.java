package app.campaign.service;

import app.campaign.model.Campaign;
import app.campaign.model.CampaignStatus;
import app.campaign.repository.CampaignRepository;
import app.user.model.User;
import app.web.dto.CampaignCreationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public void createCampaign(User user, CampaignCreationRequest campaignCreationRequest, MultipartFile file) {
        try {
            Campaign campaign = Campaign.builder()
                    .creator(user)
                    .title(campaignCreationRequest.getTitle())
                    .description(campaignCreationRequest.getDescription())
                    .shortDescription(campaignCreationRequest.getShortDescription())
                    .location(campaignCreationRequest.getLocation())
                    .address(campaignCreationRequest.getAddress())
                    .type(campaignCreationRequest.getType())
                    .status(CampaignStatus.ACTIVE)
                    .startDate(campaignCreationRequest.getStartDate())
                    .startTime(campaignCreationRequest.getStartTime())
                    .endDate(campaignCreationRequest.getEndDate())
                    .endTime(campaignCreationRequest.getEndTime())
                    .peopleNeeded(campaignCreationRequest.getPeopleNeeded())
                    .pictureData(file.getBytes())
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();

            campaign = campaignRepository.save(campaign);
            log.info("Campaign with id [%s] and type [%s] has been created successfully.".formatted(campaign.getId(), campaign.getType()));
        } catch (IOException e) {
            log.error("Can't create campaign for user with id [%s].".formatted(user.getId()), e);
            throw new RuntimeException("Unable to save campaign for user with id: [%s]. Please try again.".formatted(user.getId()));
        }
    }
}
