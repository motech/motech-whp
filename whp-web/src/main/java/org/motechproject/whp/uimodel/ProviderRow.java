package org.motechproject.whp.uimodel;

import lombok.Data;
import org.motechproject.whp.user.domain.Provider;

@Data
public class ProviderRow {

    private String providerId;
    private String district;
    private String primaryMobile;
    private String secondaryMobile;
    private String tertiaryMobile;
    private boolean active;

    public ProviderRow(Provider provider, boolean active) {
        this.providerId = provider.getProviderId();
        this.district = provider.getDistrict();
        this.primaryMobile = provider.getPrimaryMobile();
        this.secondaryMobile = provider.getSecondaryMobile();
        this.tertiaryMobile = provider.getTertiaryMobile();
        this.active = active;
    }
}
