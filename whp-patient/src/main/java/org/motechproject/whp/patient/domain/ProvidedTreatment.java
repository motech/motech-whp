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
    private SmearTestResults smearTestResults = new SmearTestResults();
    private WeightStatistics weightStatistics = new WeightStatistics();
    private TreatmentInterruptions interruptions = new TreatmentInterruptions();

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
        this.smearTestResults = oldProvidedTreatment.getSmearTestResults();
        this.weightStatistics = oldProvidedTreatment.getWeightStatistics();
    }

    public ProvidedTreatment updateForTransferIn(String tbId, String providerId, LocalDate startDate) {
        this.tbId = tbId;
        this.providerId = providerId;
        this.startDate = startDate;
        return this;
    }

    public void close(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        endDate = dateModified.toLocalDate();
        this.treatmentOutcome = treatmentOutcome;
        treatment.close(dateModified);
    }

    public void pause(String reasonForPause, DateTime dateModified) {
        interruptions.add(new TreatmentInterruption(reasonForPause, dateModified.toLocalDate()));
    }

    public void resume(String reasonForResumption, DateTime dateModified) {
        interruptions.latestInterruption().resumeTreatment(reasonForResumption, dateModified.toLocalDate());
    }

    @JsonIgnore
    public Treatment getTreatment() {
        return treatment;
    }

    @JsonIgnore
    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
        this.treatmentDocId = treatment.getId();
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
        return !weightStatistics.isEmpty() && weightStatistics.latestResult().isValid(errorCodes);
    }

    private boolean areSmearInstancesValid(List<WHPErrorCode> errorCodes) {
        return !smearTestResults.isEmpty() && smearTestResults.latestResult().isValid(errorCodes);
    }

    @JsonIgnore
    public boolean isClosed() {
        return treatment.isClosed();
    }

    public void addSmearTestResult(SmearTestRecord smearTestRecord) {
        smearTestResults.add(smearTestRecord);
    }

    public void addWeightStatistics(WeightStatisticsRecord weightStatisticsRecord) {
        weightStatistics.add(weightStatisticsRecord);
    }
}
