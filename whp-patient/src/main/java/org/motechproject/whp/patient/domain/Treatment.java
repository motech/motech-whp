package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.common.util.WHPDateUtil;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class Treatment {

    private String providerId;
    private String providerDistrict;
    private String tbId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Address patientAddress;
    private TreatmentOutcome treatmentOutcome;
    private PatientType patientType;
    private String tbRegistrationNumber;
    private SmearTestResults smearTestResults = new SmearTestResults();
    private WeightStatistics weightStatistics = new WeightStatistics();
    private TreatmentInterruptions interruptions = new TreatmentInterruptions();

    public Treatment() {
    }

    public Treatment(String providerId, String providerDistrict, String tbId, PatientType patientType) {
        setProviderId(providerId);
        setProviderDistrict(providerDistrict);
        setTbId(tbId);
        setPatientType(patientType);
    }

    public void close(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        endDate = dateModified.toLocalDate();
        this.treatmentOutcome = treatmentOutcome;
    }

    public void pause(String reasonForPause, DateTime dateModified) {
        interruptions.add(new TreatmentInterruption(reasonForPause, dateModified.toLocalDate()));
    }

    public void resume(String reasonForResumption, DateTime dateModified) {
        interruptions.latestInterruption().resumeTreatment(reasonForResumption, dateModified.toLocalDate());
    }

    public void addSmearTestResult(SmearTestRecord smearTestRecord) {
        smearTestResults.add(smearTestRecord);
    }

    public void addWeightStatistics(WeightStatisticsRecord weightStatisticsRecord) {
        weightStatistics.add(weightStatisticsRecord);
    }

    public boolean isDateInTreatment(LocalDate date) {
        if (WHPDateUtil.isOnOrAfter(date, startDate)) {
            if (endDate == null || WHPDateUtil.isOnOrBefore(date, endDate))
                return true;
        }
        return false;
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

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> errorCodes) {
        return patientAddress.isValid(errorCodes)
                && areSmearInstancesValid(errorCodes)
                && areWeightInstancesValid(errorCodes);
    }

    private boolean areWeightInstancesValid(List<WHPErrorCode> errorCodes) {
        return weightStatistics.isEmpty() || weightStatistics.latestResult().isValid(errorCodes);
    }

    private boolean areSmearInstancesValid(List<WHPErrorCode> errorCodes) {
        return smearTestResults.isEmpty() || smearTestResults.latestResult().isValid(errorCodes);
    }

    public boolean isDoseDateInPausedPeriod(LocalDate doseDate) {
        for (TreatmentInterruption interruption : getInterruptions()) {
            if (interruption.isDoseDateInInterruptionPeriod(doseDate)) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public String getStartDateAsString() {
        return WHPDate.date(startDate).value();
    }

    @JsonIgnore
    public SmearTestResult getPreTreatmentSmearTestResult() {
        return smearTestResults.getPreTreatmentResult();
    }

    public boolean hasPreTreatmentResult() {
        return smearTestResults.hasPreTreatmentResult();
    }
}
