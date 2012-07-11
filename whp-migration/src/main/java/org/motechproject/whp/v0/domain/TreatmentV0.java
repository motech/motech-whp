package org.motechproject.whp.v0.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.v0.exception.WHPErrorCodeV0;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class TreatmentV0 {

    private String providerId;
    private String tbId;
    private LocalDate startDate;
    private LocalDate endDate;
    private AddressV0 patientAddress;
    private String therapyDocId;
    private TreatmentOutcome treatmentOutcome;
    private PatientTypeV0 patientType;
    private String tbRegistrationNumber;
    private SmearTestResultsV0 smearTestResults = new SmearTestResultsV0();
    private WeightStatisticsV0 weightStatistics = new WeightStatisticsV0();
    private TreatmentInterruptionsV0 interruptions = new TreatmentInterruptionsV0();

    private TherapyV0 therapy;

    // Required for ektorp
    public TreatmentV0() {
    }

    public TreatmentV0(String providerId, String tbId, PatientTypeV0 patientType) {
        setProviderId(providerId);
        setTbId(tbId);
        setPatientType(patientType);
    }

    public TreatmentV0 updateForTransferIn(String newTbId, String newProviderId, LocalDate startDate, TreatmentV0 oldTreatment) {
        setTbId(newTbId);
        setProviderId(newProviderId);
        setStartDate(startDate);
        setTherapy(oldTreatment.getTherapy());
        setPatientAddress(oldTreatment.getPatientAddress());
        return this;
    }

    public void close(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        endDate = dateModified.toLocalDate();
        this.treatmentOutcome = treatmentOutcome;
        therapy.close(dateModified);
    }

    public void pause(String reasonForPause, DateTime dateModified) {
        interruptions.add(new TreatmentInterruptionV0(reasonForPause, dateModified.toLocalDate()));
    }

    public void resume(String reasonForResumption, DateTime dateModified) {
        interruptions.latestInterruption().resumeTreatment(reasonForResumption, dateModified.toLocalDate());
    }

    @JsonIgnore
    public TherapyV0 getTherapy() {
        return therapy;
    }

    @JsonIgnore
    public void setTherapy(TherapyV0 therapy) {
        this.therapy = therapy;
        this.therapyDocId = therapy.getId();
    }

    @JsonIgnore
    public boolean isPaused() {
        return !CollectionUtils.isEmpty(interruptions) && interruptions.latestInterruption().isCurrentlyPaused();
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCodeV0> errorCodes) {
        return patientAddress.isValid(errorCodes)
                && areSmearInstancesValid(errorCodes)
                && areWeightInstancesValid(errorCodes);
    }

    private boolean areWeightInstancesValid(List<WHPErrorCodeV0> errorCodes) {
        return weightStatistics.isEmpty() || weightStatistics.latestResult().isValid(errorCodes);
    }

    private boolean areSmearInstancesValid(List<WHPErrorCodeV0> errorCodes) {
        return smearTestResults.isEmpty() || smearTestResults.latestResult().isValid(errorCodes);
    }

    @JsonIgnore
    public boolean isClosed() {
        return therapy.isClosed();
    }

    public void addSmearTestResult(SmearTestRecordV0 smearTestRecord) {
        smearTestResults.add(smearTestRecord);
    }

    public void addWeightStatistics(WeightStatisticsRecordV0 weightStatisticsRecord) {
        weightStatistics.add(weightStatisticsRecord);
    }

    public void setProviderId(String providerId) {
        if (providerId == null)
            this.providerId = null;
        else
            this.providerId = providerId.toLowerCase();
    }

    public void setTbId(String tbId) {
        if (tbId == null)
            this.tbId = null;
        else
            this.tbId = tbId.toLowerCase();
    }
}