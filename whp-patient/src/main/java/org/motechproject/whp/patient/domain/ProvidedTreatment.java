package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;

@Data
public class ProvidedTreatment {

    private String providerId;
    private String tbId;
    private LocalDate startDate;
    private LocalDate endDate;

    private Address patientAddress;

    private String treatmentDocId;

    @JsonIgnore
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

}
