package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;

import javax.validation.constraints.NotNull;

public class TBId {

    @JsonProperty
    @NotNull
    private String providerId;

    @JsonProperty
    @NotNull
    private String tbId;

    @JsonProperty
    private LocalDate startDate;

    @JsonProperty
    private LocalDate endDate;

    public TBId() {
    }

    public TBId(String tbId, String providerId) {
        this.setTbId(tbId);
        this.setProviderId(providerId);
        this.setStartDate(DateUtil.today());
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getTbId() {
        return tbId;
    }

    public void setTbId(String tbId) {
        this.tbId = tbId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
