package org.motechproject.whp.importer.csv.request;

import org.motechproject.importer.annotation.ColumnName;
import org.motechproject.validation.constraints.NotNullOrEmpty;

import javax.validation.constraints.Pattern;

public class ImportProviderRequest {
    @NotNullOrEmpty
    @ColumnName(name = "Provider ID*")
    private String providerId;

    @NotNullOrEmpty
    @ColumnName(name = "District*")
    private String district;

    @NotNullOrEmpty
    @ColumnName(name = "Primary Mobile Number*")
    @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number should have 10 digits")
    private String primaryMobile;

    @ColumnName(name = "Secondary Mobile Number")
    @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number should have 10 digits")
    private String secondaryMobile;

    @ColumnName(name = "Tertiary Mobile Number")
    @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number should have 10 digits")
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
