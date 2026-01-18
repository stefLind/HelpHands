package app.web.dto;

import app.util.StringUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CampaignFilterData {

    private String status;

    private String type;

    private String creator;

    private String location;

    public CampaignFilterData(String status, String type, String creator, String location) {
        this.status = StringUtil.isNullOrBlank(status) ? null : status;
        this.type = StringUtil.isNullOrBlank(type) ? null : type;
        this.creator = StringUtil.isNullOrBlank(creator) ? null : creator;
        this.location = StringUtil.isNullOrBlank(location) ? null : location;
    }

    public String toQueryString() {
        String queryString = "location=";

        if (StringUtil.isNotNullOrBlank(getLocation())) {
            queryString += getLocation();
        }
        queryString += "&type=";
        if (StringUtil.isNotNullOrBlank(getType())) {
            queryString += getType();
        }
        queryString += "&status=";
        if (StringUtil.isNotNullOrBlank(getStatus())) {
            queryString += getStatus();
        }
        queryString += "&creator=";
        if (StringUtil.isNotNullOrBlank(getCreator())) {
            queryString += getCreator();
        }

        return queryString;
    }
}
