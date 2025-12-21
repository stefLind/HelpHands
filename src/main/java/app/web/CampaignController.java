package app.web;

import app.campaign.service.CampaignService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CampaignCreationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Controller
@RequestMapping()
public class CampaignController {


    private final UserService userService;
    private final CampaignService campaignService;

    @Autowired
    public CampaignController(UserService userService, CampaignService campaignService) {
        this.userService = userService;
        this.campaignService = campaignService;
    }

    @GetMapping("/campaigns")
    public ModelAndView getCampaignsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("campaigns");
        if (authenticationMetadata != null) {
            User user = userService.getUserById(authenticationMetadata.getUserId());
            modelAndView.addObject("user", user);
        }

        return modelAndView;
    }

    @GetMapping("/campaign/new")
    public ModelAndView getCreateCampaignPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getUserById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create-campaign");
        modelAndView.addObject("user", user);
        modelAndView.addObject("campaignCreationRequest", new CampaignCreationRequest());

        return modelAndView;
    }

    @PostMapping(path = "/campaign/new", consumes = MULTIPART_FORM_DATA_VALUE)
    public ModelAndView createCampaign(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid CampaignCreationRequest campaignCreationRequest, BindingResult bindingResult, @RequestPart("file") MultipartFile file) {
        User user = userService.getUserById(authenticationMetadata.getUserId());
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("create-campaign");
            modelAndView.addObject("user", user);
            modelAndView.addObject("campaignCreationRequest", campaignCreationRequest);

            return modelAndView;
        }

        campaignService.createCampaign(user, campaignCreationRequest, file);

        return new ModelAndView("redirect:/campaigns");
    }
}
