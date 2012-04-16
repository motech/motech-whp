package org.motechproject.whp.provider.domain;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/16/12
 * Time: 4:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class WHPProvider {
    private String primary_mobile;
    private String secondary_mobile;
    private String tertiary_mobile;
    private String provider_id;
    private String district;

    public String getPrimary_mobile() {
        return primary_mobile;
    }

    public void setPrimary_mobile(String primary_mobile) {
        this.primary_mobile = primary_mobile;
    }

    public String getSecondary_mobile() {
        return secondary_mobile;
    }

    public void setSecondary_mobile(String secondary_mobile) {
        this.secondary_mobile = secondary_mobile;
    }

    public String getTertiary_mobile() {
        return tertiary_mobile;
    }

    public void setTertiary_mobile(String tertiary_mobile) {
        this.tertiary_mobile = tertiary_mobile;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
