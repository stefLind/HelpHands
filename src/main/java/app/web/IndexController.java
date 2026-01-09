package app.web;

import app.campaign.model.Campaign;
import app.campaign.service.CampaignService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {

    UserService userService;
    CampaignService campaignService;

    @Autowired
    public IndexController(UserService userService, CampaignService campaignService) {
        this.userService = userService;
        this.campaignService = campaignService;
    }

    @GetMapping("/")
    public ModelAndView getIndexPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("index");
        if (authenticationMetadata != null) {
            User user = userService.getUserById(authenticationMetadata.getUserId());
            modelAndView.addObject("user", user);
        }

        List<Campaign> lastThreeActiveCampaigns = campaignService.getLastThreeActiveCampaigns();
        modelAndView.addObject("lastThreeActiveCampaigns", lastThreeActiveCampaigns);

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "errorParam", required = false) String errorParam) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        if (errorParam != null) {
            modelAndView.addObject("errorMessage", "Incorrect username or password!");
        }

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }

        userService.register(registerRequest);

        return new ModelAndView("redirect:/login");
    }
}
