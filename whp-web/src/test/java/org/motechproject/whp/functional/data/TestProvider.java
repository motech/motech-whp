package org.motechproject.whp.functional.data;


import lombok.Data;

@Data
public class TestProvider {

    private String providerId;
    private String password;
    private String district;
    private String primaryMobile;
    private String secondaryMobile;
    private String tertiaryMobile;

    public TestProvider(String providerId, String password, String district, String primaryMobile, String secondaryMobile, String tertiaryMobile) {
        this.providerId = providerId;
        this.password = password;
        this.district = district;
        this.primaryMobile = primaryMobile;
        this.secondaryMobile = secondaryMobile;
        this.tertiaryMobile = tertiaryMobile;
    }

    public String getId() {
        return providerId.toLowerCase();
    }
}

