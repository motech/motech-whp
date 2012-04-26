package org.motechproject.whp.patient.domain;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;


public class ProvidedTreatment {

    @Getter
    @Setter
    private String providerId;
    @Getter
    @Setter
    private String tbId;
    @Getter
    @Setter
    private LocalDate startDate;
    @Getter
    @Setter
    private LocalDate endDate;

    @Getter
    @Setter
    private Address patientAddress;

    @Getter
    @Setter
    private String treatmentDocId;

    private Treatment treatment;

    public ProvidedTreatment() {
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
        this.treatmentDocId = treatment.getId();
    }

    public ProvidedTreatment(String providerId, String tbId, LocalDate startDate) {
        this.providerId = providerId;
        this.tbId = tbId;
        this.startDate = startDate;
    }

    @JsonIgnore
    public Treatment getTreatment() {
        return treatment;
    }
}
