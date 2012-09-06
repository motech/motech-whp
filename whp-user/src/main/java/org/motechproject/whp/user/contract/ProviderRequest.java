package org.motechproject.whp.user.contract;

import lombok.Data;
import org.joda.time.DateTime;
import org.motechproject.whp.user.domain.Provider;

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
        setProviderId(providerId);
        this.district = district;
        this.primaryMobile = primaryMobile;
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setProviderId(String providerId) {
        if (providerId != null)
            this.providerId = providerId.toLowerCase();
    }

    public Provider makeProvider() {
        Provider provider = new Provider(getProviderId(), getPrimaryMobile(), getDistrict(), getLastModifiedDate());
        provider.setSecondaryMobile(getSecondaryMobile());
        provider.setTertiaryMobile(getTertiaryMobile());
        return provider;
    }
}
