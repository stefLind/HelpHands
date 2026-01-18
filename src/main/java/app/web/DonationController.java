package app.web;

import app.campaign.model.Campaign;
import app.campaign.service.CampaignService;
import app.donation.model.Donation;
import app.donation.service.DonationService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.DonationFilterData;
import app.web.dto.DonationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping()
public class DonationController {

    private final UserService userService;
    private final CampaignService campaignService;
    private final DonationService donationService;

    @Autowired
    public DonationController(UserService userService, CampaignService campaignService, DonationService donationService) {
        this.userService = userService;
        this.campaignService = campaignService;
        this.donationService = donationService;
    }

    @PostMapping("/{id}/donation/new")
    public ModelAndView donate(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid DonationRequest donationRequest) {
        User user = userService.getUserById(authenticationMetadata.getUserId());
        Campaign campaign = campaignService.getCampaignById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/campaign/" + campaign.getId());
        modelAndView.addObject("user", user);
        modelAndView.addObject("campaign", campaign);
        modelAndView.addObject("donationRequest", donationRequest);

        donationService.createDonation(user, campaign, donationRequest);

        return modelAndView;

    }

    @GetMapping("/my-donations")
    public ModelAndView getDonations(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata
            , @RequestParam(name = "donationStatus", required = false) String donationStatus
            , @RequestParam(name = "campaignStatus", required = false) String campaignStatus
            , @RequestParam(name = "campaignType", required = false) String campaignType) {
        User user = userService.getUserById(authenticationMetadata.getUserId());

        DonationFilterData filterData = new DonationFilterData(donationStatus, campaignStatus, campaignType);
        List<Donation> donations = donationService.filterDonations(filterData, user.getDonations());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("my-donations");
        modelAndView.addObject("user", user);
        modelAndView.addObject("donations", donations);
        modelAndView.addObject("filterData", filterData);

        return modelAndView;
    }

}
