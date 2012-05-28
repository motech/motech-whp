package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.springframework.util.CollectionUtils;

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
    private String tbRegistrationNumber;

    private SmearTestInstances smearTestInstances = new SmearTestInstances();
    private WeightInstances weightInstances = new WeightInstances();
    private TreatmentInterruptions interruptions = new TreatmentInterruptions();

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
        this.smearTestInstances = oldProvidedTreatment.getSmearTestInstances();
        this.weightInstances = oldProvidedTreatment.getWeightInstances();
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
        interruptions.add(new TreatmentInterruption(reasonForPause, dateModified.toLocalDate()));
    }

    public void resume(String reasonForResumption, DateTime dateModified) {
        interruptions.latestInterruption().resumeTreatment(reasonForResumption, dateModified.toLocalDate());
    }

    @JsonIgnore
    public boolean isPaused() {
        return !CollectionUtils.isEmpty(interruptions) && interruptions.latestInterruption().isCurrentlyPaused();
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> errorCodes) {
        return patientAddress.isValid(errorCodes)
                && areSmearInstancesValid(errorCodes)
                && areWeightInstancesValid(errorCodes);
    }

    private boolean areWeightInstancesValid(List<WHPErrorCode> errorCodes) {
        return !CollectionUtils.isEmpty(weightInstances) && weightInstances.latestResult().isValid(errorCodes);
    }

    private boolean areSmearInstancesValid(List<WHPErrorCode> errorCodes) {
        return !smearTestInstances.isEmpty() && smearTestInstances.latestResult().isValid(errorCodes);
    }

    @JsonIgnore
    public boolean isClosed() {
        return treatmentOutcome != null;
    }

    public void addSmearTestResult(SmearTestResults smearTestResults) {
        smearTestInstances.add(smearTestResults);
    }

    public void addWeightStatistics(WeightStatistics weightStatistics) {
        weightInstances.add(weightStatistics);
    }

}
