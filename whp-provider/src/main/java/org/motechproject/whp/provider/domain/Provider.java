package org.motechproject.whp.provider.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type == 'Provider'")
public class Provider extends MotechBaseDataObject {

    @JsonProperty
    @NotEmpty
    private String primaryMobile;

    @JsonProperty
    private String secondaryMobile;

    @JsonProperty
    private String tertiaryMobile;

    @JsonProperty
    @NotEmpty
    private String providerId;

    @JsonProperty
    @NotEmpty
    private String district;

    // Required for ektorp
    public Provider() {
    }

    public Provider(String providerId, String primaryMobile, String district) {
        this.providerId = providerId;
        this.primaryMobile = primaryMobile;
        this.district = district;
    }

    public String getPrimaryMobile() {
        return primaryMobile;
    }

    public void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile = primaryMobile;
    }

    public String getSecondaryMobile() {
        return secondaryMobile;
    }

    public void setSecondaryMobile(String secondaryMobile) {
        this.secondaryMobile = secondaryMobile;
    }

    public String getTertiaryMobile() {
        return tertiaryMobile;
    }

    public void setTertiaryMobile(String tertiaryMobile) {
        this.tertiaryMobile = tertiaryMobile;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
