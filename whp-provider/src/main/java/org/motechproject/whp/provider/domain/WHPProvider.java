package org.motechproject.whp.provider.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/12/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeDiscriminator("doc.type == 'WHPProvider'")
public class WHPProvider extends MotechBaseDataObject {
    @JsonProperty
    private String primaryMobile;
    @JsonProperty
    private String secondaryMobile;
    @JsonProperty
    private String tertiaryMobile;
    @JsonProperty
    private String providerId;
    @JsonProperty
    private String district;

    public WHPProvider(String providerId, String primaryMobile) {
        this.providerId = providerId;
        this.primaryMobile = primaryMobile;
    }

    public WHPProvider() {
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
