package org.motechproject.whp.provider.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;

@Data
@TypeDiscriminator("doc.type == 'Provider'")
public class Provider extends MotechBaseDataObject {

    private String primaryMobile;

    private String secondaryMobile;

    private String tertiaryMobile;

    private String providerId;

    private String district;

    private DateTime lastModifiedDate;

    // Required for ektorp
    public Provider() {
    }

    public Provider(String providerId, String primaryMobile, String district, DateTime lastModifiedDate) {
        this.providerId = providerId;
        this.primaryMobile = primaryMobile;
        this.district = district;
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getPrimaryMobile() {
        return primaryMobile;
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

    public String getDistrict() {
        return district;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
