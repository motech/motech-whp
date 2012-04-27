package org.motechproject.whp.patient.contract;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class ProviderRequest {

    private String providerId;

    private String district;

    private String primaryMobile;

    private String secondaryMobile;

    private String tertiaryMobile;

    private String username;
    private String password;
    private String uuid;

    private DateTime lastModifiedDate;

    public ProviderRequest(String providerId, String district, String primaryMobile, DateTime lastModifiedDate) {
        this.providerId = providerId;
        this.district = district;
        this.primaryMobile = primaryMobile;
        this.lastModifiedDate = lastModifiedDate;
    }

}
