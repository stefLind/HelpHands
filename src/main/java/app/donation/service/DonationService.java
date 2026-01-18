package app.donation.service;

import app.campaign.model.Campaign;
import app.donation.model.Donation;
import app.donation.model.DonationStatus;
import app.donation.repository.DonationRepository;
import app.exception.DomainException;
import app.user.model.User;
import app.util.StringUtil;
import app.web.dto.DonationFilterData;
import app.web.dto.DonationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DonationService {

    private final DonationRepository donationRepository;

    @Autowired
    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public void createDonation(User user, Campaign campaign, DonationRequest donationRequest) {
        Donation donation = Donation.builder()
                .owner(user)
                .campaign(campaign)
                .status(DonationStatus.PENDING)
                .message(donationRequest.getMessage())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        donationRepository.save(donation);

    }

    public int getPercentageOfParticipants(Integer targetParticipants, int numberOfParticipants) {
        if (targetParticipants == null || targetParticipants < 1) {
            return 0;
        }
        return (numberOfParticipants * 100) / targetParticipants;
    }

    public List<Donation> filterDonations(DonationFilterData filterData, List<Donation> donations) {
        if (donations.isEmpty() || StringUtil.isAllNullOrBlank(filterData.getDonationStatus(), filterData.getCampaignStatus(), filterData.getCampaignType())) {
            return donations;
        }

        List<Donation> filteredDonations = donations;
        if (StringUtil.isNotNullOrBlank(filterData.getDonationStatus())) {
            filteredDonations = filteredDonations.stream()
                    .filter(donation -> filterData.getDonationStatus().equals(donation.getStatus().name()))
                    .toList();
        }
        if (StringUtil.isNotNullOrBlank(filterData.getCampaignStatus())) {
            filteredDonations = filteredDonations.stream()
                    .filter(donation -> filterData.getCampaignStatus().equals(donation.getCampaign().getStatus().name()))
                    .toList();
        }
        if (StringUtil.isNotNullOrBlank(filterData.getCampaignType())) {
            filteredDonations = filteredDonations.stream()
                    .filter(donation -> filterData.getCampaignType().equals(donation.getCampaign().getType().name()))
                    .toList();
        }

        return filteredDonations;
    }

    public Donation getDonationById(UUID id) {
        return donationRepository.findById(id).orElseThrow(() -> new DomainException("Donation with id [" + id + "] not found."));
    }
}
