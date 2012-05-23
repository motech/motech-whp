package org.motechproject.whp.importer.csv.request;

import org.hibernate.validator.constraints.NotEmpty;
import org.motechproject.importer.annotation.ColumnName;

public class ImportProviderRequest {
    @NotEmpty
    @ColumnName(name = "Provider ID*")
    private String providerId;

    @NotEmpty
    @ColumnName(name = "District*")
    private String district;


    @NotEmpty
    @ColumnName(name = "Primary Mobile Number*")
    private String primaryMobile;

    @ColumnName(name = "Secondary Mobile Number")
    private String secondaryMobile;

    @ColumnName(name = "Tertiary Mobile Number")
    private String tertiaryMobile;

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


}
