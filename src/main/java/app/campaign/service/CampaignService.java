package app.campaign.service;

import app.campaign.model.Campaign;
import app.campaign.model.CampaignStatus;
import app.campaign.repository.CampaignRepository;
import app.email.service.EmailService;
import app.exception.DomainException;
import app.user.model.User;
import app.util.DateUtil;
import app.util.StringUtil;
import app.web.dto.CampaignFilterData;
import app.web.dto.CampaignModificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CampaignService {

    private static final int PAGE_SIZE = 6;
    private final CampaignRepository campaignRepository;
    private final EmailService emailService;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, EmailService emailService) {
        this.campaignRepository = campaignRepository;
        this.emailService = emailService;
    }

    public void createCampaign(User user, CampaignModificationRequest campaignCreationRequest, MultipartFile file) {
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
                    .endDate(campaignCreationRequest.getEndDate())
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

    public Campaign getCampaignById(UUID id) {
        return campaignRepository.findById(id).orElseThrow(() -> new DomainException("Campaign with id [" + id + "] not found."));
    }

    public long getDaysLeftAsPercentage(Campaign campaign) {
        long maxPercent = 100L;
        long daysLeft = DateUtil.getDateDiffFromNowInDays(campaign.getStartDate());

        if (daysLeft <= 0) {
            return maxPercent;
        } else if (daysLeft >= maxPercent) {
            return 1L;
        } else {
            return maxPercent - daysLeft;
        }
    }

    public void editCampaign(UUID id, CampaignModificationRequest campaignModificationRequest, MultipartFile file) {
        Campaign campaign = getCampaignById(id);
        boolean hasToSendMail = hasToSendMail(campaign, campaignModificationRequest);
        try {
            String emailSubject = hasToSendMail ? "HelpHands: Промени в кампания '%s'".formatted(campaign.getTitle()) : "";
            String emailBody = hasToSendMail ? getEmailBody(campaign, campaignModificationRequest) : "";

            campaign.setLocation(campaignModificationRequest.getLocation());
            campaign.setAddress(campaignModificationRequest.getAddress());
            campaign.setStartDate(campaignModificationRequest.getStartDate());
            campaign.setEndDate(campaignModificationRequest.getEndDate());
            campaign.setPeopleNeeded(campaignModificationRequest.getPeopleNeeded());
            campaign.setThingsNeeded(campaignModificationRequest.getThingsNeeded());
            campaign.setFoodNeeded(campaignModificationRequest.getFoodNeeded());
            campaign.setUpdatedOn(LocalDateTime.now());

            if (!file.isEmpty()) {
                campaign.setPictureData(file.getBytes());
            }

            if (hasToSendMail) {
                emailService.sendEmailToUsers(getDonatorIds(campaign), emailSubject, emailBody);
            }

            campaign = campaignRepository.save(campaign);
            log.info("Campaign with id [%s] and type [%s] has been updated successfully.".formatted(campaign.getId(), campaign.getType()));
        } catch (IOException e) {
            log.error("Can't update campaign for user with id [%s].".formatted(campaign.getCreator().getId()), e);
            throw new RuntimeException("Unable to save campaign for user with id: [%s]. Please try again.".formatted(campaign.getCreator().getId()));
        }
    }

    private String getEmailBody(Campaign campaign, CampaignModificationRequest campaignModificationRequest) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        StringBuilder emailBody = new StringBuilder("<h4>Направени са промени в кампания '%s'.</h4> <p>Вижте промените по-долу:</p><ul>".formatted(campaign.getTitle()));
        if (campaignModificationRequest.getStartDate() != null && !campaign.getStartDate().isEqual(campaignModificationRequest.getStartDate())) {
            emailBody.append("<li><b>Начална дата:</b> ").append(dateTimeFormatter.format(campaignModificationRequest.getStartDate())).append("</li>");
        }
        if (campaignModificationRequest.getEndDate() != null && !campaign.getEndDate().isEqual(campaignModificationRequest.getEndDate())) {
            emailBody.append("<li><b>Крайна дата:</b> ").append(dateTimeFormatter.format(campaignModificationRequest.getEndDate())).append("</li>");
        }
        if (!Objects.equals(campaignModificationRequest.getLocation(), campaign.getLocation())) {
            emailBody.append("<li><b>Град:</b> ").append(campaignModificationRequest.getLocation()).append("</li>");
        }
        if (!Objects.equals(campaignModificationRequest.getAddress(), campaign.getAddress())) {
            emailBody.append("<li><b>Адрес:</b> ").append(campaignModificationRequest.getAddress()).append("</li>");
        }
        if (!Objects.equals(campaignModificationRequest.getThingsNeeded(), campaign.getThingsNeeded())) {
            String thingsNeeded = StringUtil.isNotNullOrBlank(campaignModificationRequest.getThingsNeeded()) ? campaignModificationRequest.getThingsNeeded() : "Не е зададено";
            emailBody.append("<li><b>Нужни вещи:</b> ").append(thingsNeeded).append("</li>");
        }
        if (!Objects.equals(campaignModificationRequest.getFoodNeeded(), campaign.getFoodNeeded())) {
            String foodNeeded = StringUtil.isNotNullOrBlank(campaignModificationRequest.getFoodNeeded()) ? campaignModificationRequest.getFoodNeeded() : "Не е зададено";
            emailBody.append("<li><b>Нужна храна:</b> ").append(foodNeeded).append("</li>");
        }
        if (!Objects.equals(campaignModificationRequest.getPeopleNeeded(), campaign.getPeopleNeeded())) {
            Integer peopleNeeded = campaignModificationRequest.getPeopleNeeded() != null ? campaignModificationRequest.getPeopleNeeded() : 0;
            emailBody.append("<li><b>Брой хора:</b> ").append(peopleNeeded).append("</li>");
        }

        emailBody.append("</ul><p>Екипът на <b>HelpHands</b></p>");

        return emailBody.toString();
    }

    private boolean hasToSendMail(Campaign campaign, CampaignModificationRequest campaignModificationRequest) {
        return (campaignModificationRequest.getStartDate() != null && !campaign.getStartDate().isEqual(campaignModificationRequest.getStartDate())) ||
                (campaignModificationRequest.getEndDate() != null && !campaign.getEndDate().isEqual(campaignModificationRequest.getEndDate())) ||
                !Objects.equals(campaignModificationRequest.getLocation(), campaign.getLocation()) ||
                !Objects.equals(campaignModificationRequest.getAddress(), campaign.getAddress()) ||
                !Objects.equals(campaignModificationRequest.getThingsNeeded(), campaign.getThingsNeeded()) ||
                !Objects.equals(campaignModificationRequest.getFoodNeeded(), campaign.getFoodNeeded()) ||
                !Objects.equals(campaignModificationRequest.getPeopleNeeded(), campaign.getPeopleNeeded());
    }

    private Set<UUID> getDonatorIds(Campaign campaign) {
        return campaign.getDonations()
                .stream()
                .filter(donation -> donation.getOwner() != null)
                .map(donation -> donation.getOwner().getId())
                .collect(Collectors.toSet());
    }

}
