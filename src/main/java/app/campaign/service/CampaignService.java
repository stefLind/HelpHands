package app.campaign.service;

import app.campaign.model.Campaign;
import app.campaign.model.CampaignStatus;
import app.campaign.repository.CampaignRepository;
import app.user.model.User;
import app.util.StringUtil;
import app.web.dto.CampaignCreationRequest;
import app.web.dto.CampaignFilterData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CampaignService {

    private static final int PAGE_SIZE = 6;
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
                    .thingsNeeded(campaignCreationRequest.getThingsNeeded())
                    .foodNeeded(campaignCreationRequest.getFoodNeeded())
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

    public Page<Campaign> getCampaignsPage(int currentPage, CampaignFilterData campaignFilterData) {
        List<Campaign> list;

        List<Campaign> campaigns = filterCampaigns(campaignFilterData.getStatus(), campaignFilterData.getType(), campaignFilterData.getCreator(), campaignFilterData.getLocation());

        if (campaigns.size() <= PAGE_SIZE) {
            list = campaigns;
        } else {
            int startItem = (currentPage - 1) * PAGE_SIZE;
            int toIndex = Math.min(startItem + PAGE_SIZE, campaigns.size());
            list = campaigns.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage - 1, PAGE_SIZE), campaigns.size());
    }

    private List<Campaign> filterCampaigns(String status, String type, String creator, String location) {
        List<Campaign> campaigns;
        if (StringUtil.isAllNullOrBlank(status, type, creator, location)) {
            campaigns = getAllCampaigns();
        } else {
            campaigns = campaignRepository.findByFilterCriteria(status, type, creator, location);
        }
        return campaigns;
    }

    private List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public List<Campaign> getLastThreeActiveCampaigns() {
        return campaignRepository.findAllByStatusOrderByCreatedOnDesc(CampaignStatus.ACTIVE)
                .stream()
                .limit(3)
                .toList();
    }
}
