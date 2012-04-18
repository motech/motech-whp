package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
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
    @JsonProperty
    private Address address;

    public Treatment() {
    }

    public Treatment(Category category, String providerId, String tbId) {
        this.setCategory(category);
        this.addTBId(tbId, providerId);
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
