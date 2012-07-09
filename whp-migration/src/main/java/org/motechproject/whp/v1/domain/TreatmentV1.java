package org.motechproject.whp.v1.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.util.CollectionUtils;

@Data
public class TreatmentV1 {

    private String providerId;
    private String tbId;
    private LocalDate startDate;
    private LocalDate endDate;
    private AddressV1 patientAddress;
    private TreatmentOutcomeV1 treatmentOutcome;
    private PatientTypeV1 patientType;
    private String tbRegistrationNumber;
    private SmearTestResultsV1 smearTestResults = new SmearTestResultsV1();
    private WeightStatisticsV1 weightStatistics = new WeightStatisticsV1();
    private TreatmentInterruptionsV1 interruptions = new TreatmentInterruptionsV1();

    public TreatmentV1() {
    }

    public TreatmentV1(String providerId, String tbId, PatientTypeV1 patientType) {
        setProviderId(providerId);
        setTbId(tbId);
        setPatientType(patientType);
    }

    public TreatmentV1 updateForTransferIn(String newTbId, String newProviderId, LocalDate startDate, TreatmentV1 oldTreatment) {
        setTbId(newTbId);
        setProviderId(newProviderId);
        setStartDate(startDate);
        setPatientAddress(oldTreatment.getPatientAddress());
        return this;
    }

    public void close(TreatmentOutcomeV1 treatmentOutcome, DateTime dateModified) {
        endDate = dateModified.toLocalDate();
        this.treatmentOutcome = treatmentOutcome;
    }

    public void pause(String reasonForPause, DateTime dateModified) {
        interruptions.add(new TreatmentInterruptionV1(reasonForPause, dateModified.toLocalDate()));
    }

    public void resume(String reasonForResumption, DateTime dateModified) {
        interruptions.latestInterruption().resumeTreatment(reasonForResumption, dateModified.toLocalDate());
    }

    public void addSmearTestResult(SmearTestRecordV1 smearTestRecord) {
        smearTestResults.add(smearTestRecord);
    }

    public void addWeightStatistics(WeightStatisticsRecordV1 weightStatisticsRecord) {
        weightStatistics.add(weightStatisticsRecord);
    }

    @JsonIgnore
    public boolean isPaused() {
        return !CollectionUtils.isEmpty(interruptions) && interruptions.latestInterruption().isCurrentlyPaused();
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

    public boolean isDoseDateInPausedPeriod(LocalDate doseDate) {
        for (TreatmentInterruptionV1 interruption : getInterruptions()) {
            if (interruption.isDoseDateInInterruptionPeriod(doseDate)) {
                return true;
            }
        }
        return false;
    }

}
