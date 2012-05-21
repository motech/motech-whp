package org.motechproject.whp.patient.domain;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.exception.errorcode.WHPDomainErrorCode;

import java.util.List;

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

    public void close(String treatmentOutcome, DateTime dateModified) {
        endDate = dateModified.toLocalDate();
        treatment.close(treatmentOutcome, dateModified);
    }

    public void pause(String reasonForPause, DateTime dateModified) {
        treatment.pause(reasonForPause, dateModified);
    }

    public void resume(String reasonForResumption, DateTime dateModified) {
        treatment.resume(reasonForResumption, dateModified);
    }

    @JsonIgnore
    public boolean isValid(List<WHPDomainErrorCode> errorCodes) {
        return treatment.isValid(errorCodes) && patientAddress.isValid(errorCodes);
    }

    @JsonIgnore
    public boolean isClosed() {
        return treatment.isClosed();
    }

    @JsonIgnore
    public boolean isPaused() {
        return treatment.isPaused();
    }
}
