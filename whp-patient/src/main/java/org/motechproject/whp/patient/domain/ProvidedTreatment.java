package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.List;

@Data
public class ProvidedTreatment {

    private String providerId;
    private String tbId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Address patientAddress;
    private String treatmentDocId;
    private TreatmentOutcome treatmentOutcome;
    private PatientType patientType;

    @JsonIgnore
    private Treatment treatment;

    // Required for ektorp
    public ProvidedTreatment() {
    }

    public ProvidedTreatment(String providerId, String tbId, PatientType patientType) {
        this.providerId = providerId;
        this.tbId = tbId;
        this.patientType = patientType;
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

    public void close(String treatmentOutcome, DateTime dateModified) {
        endDate = dateModified.toLocalDate();
        this.treatmentOutcome = TreatmentOutcome.valueOf(treatmentOutcome);
        treatment.close(dateModified);
    }

    public void pause(String reasonForPause, DateTime dateModified) {
        treatment.pause(reasonForPause, dateModified);
    }

    public void resume(String reasonForResumption, DateTime dateModified) {
        treatment.resume(reasonForResumption, dateModified);
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> errorCodes) {
        return treatment.isValid(errorCodes) && patientAddress.isValid(errorCodes);
    }

    @JsonIgnore
    public boolean isClosed() {
        return treatmentOutcome != null;
    }

    @JsonIgnore
    public boolean isPaused() {
        return treatment.isPaused();
    }

}
