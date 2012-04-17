package org.motechproject.whp.patient.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Treatment {

    @NotNull
    @JsonProperty
    private String id;
    @NotNull
    @JsonProperty
    private Category category;
    @JsonProperty
    List<TBId> tbIds = new ArrayList<TBId>();
    @NotNull
    @JsonProperty
    private TBId currentTBId;
    @JsonProperty
    private LocalDate startDate;
    @JsonProperty
    private LocalDate endDate;
    @JsonProperty
    private String registrationNumber;
    @JsonProperty
    private LocalDate registrationDate;

    public Treatment() {
    }

    public Treatment(Category category, String providerId, String tbId) {
        this.setCategory(category);
        this.addTBId(tbId, providerId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<TBId> getTbIds() {
        return tbIds;
    }

    public void setTbIds(List<TBId> tbIds) {
        this.tbIds = tbIds;
    }

    public TBId getCurrentTBId() {
        return currentTBId;
    }

    public void setCurrentTBId(TBId currentTBId) {
        this.currentTBId = currentTBId;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Treatment setRegistrationDetails(String registrationNumber, LocalDate registrationDate) {
        this.setRegistrationNumber(registrationNumber);
        this.setRegistrationDate(registrationDate);
        return this;
    }

    private Treatment addTBId(String tbId, String providerId) {
        TBId currentTBId = new TBId(tbId, providerId);
        this.tbIds.add(currentTBId);
        this.currentTBId = currentTBId;
        return this;
    }
}
