package org.motechproject.whp.patient.domain;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.repository.ValidationErrors;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import static org.motechproject.util.DateUtil.today;

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

    // Required for ektorp
    public ProvidedTreatment() {
    }

    public ProvidedTreatment(String providerId, String tbId) {
        this.providerId = providerId;
        this.tbId = tbId;
    }

    public ProvidedTreatment(ProvidedTreatment oldProvidedTreatment) {
        this.tbId = oldProvidedTreatment.tbId;
        this.providerId = oldProvidedTreatment.providerId;
        this.startDate = oldProvidedTreatment.startDate;
        this.endDate = oldProvidedTreatment.endDate;
        setTreatment(oldProvidedTreatment.getTreatment());
        this.patientAddress = oldProvidedTreatment.getPatientAddress();
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
        this.treatmentDocId = treatment.getId();
    }

    public ProvidedTreatment updateForTransferIn(String tbId, String providerId, LocalDate startDate) {
        this.tbId = tbId;
        this.providerId = providerId;
        this.startDate = startDate;
        return this;
    }

    @JsonIgnore
    public Treatment getTreatment() {
        return treatment;
    }

    @JsonIgnore
    public boolean isValid(ValidationErrors validationErrors) {
        return treatment.isValid(validationErrors) && patientAddress.isValid(validationErrors);
    }

    public void close(String treatmentOutcome) {
        endDate = today();
        treatment.close(treatmentOutcome);
    }
}
