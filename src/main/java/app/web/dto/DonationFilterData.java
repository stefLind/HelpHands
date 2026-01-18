package app.web.dto;

import app.util.StringUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class DonationFilterData {

    private String donationStatus;

    private String campaignStatus;

    private String campaignType;

    public DonationFilterData(String donationStatus, String campaignStatus, String campaignType) {
        this.donationStatus = StringUtil.isNullOrBlank(donationStatus) ? null : donationStatus;
        this.campaignStatus = StringUtil.isNullOrBlank(campaignStatus) ? null : campaignStatus;
        this.campaignType = StringUtil.isNullOrBlank(campaignType) ? null : campaignType;
    }
}
