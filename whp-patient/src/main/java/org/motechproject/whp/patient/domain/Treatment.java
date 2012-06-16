package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.util.WHPDateUtil;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class Treatment {

    private String providerId;
    private String tbId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Address patientAddress;
    private String therapyDocId;
    private TreatmentOutcome treatmentOutcome;
    private PatientType patientType;
    private String tbRegistrationNumber;
    private SmearTestResults smearTestResults = new SmearTestResults();
    private WeightStatistics weightStatistics = new WeightStatistics();
    private TreatmentInterruptions interruptions = new TreatmentInterruptions();
    private Therapy therapy;

    public Treatment() {
    }

    public Treatment(String providerId, String tbId, PatientType patientType) {
        setProviderId(providerId);
        setTbId(tbId);
        setPatientType(patientType);
    }

    public Treatment(Treatment oldTreatment) {
        setTbId(oldTreatment.tbId);
        setProviderId(oldTreatment.providerId);
        setStartDate(oldTreatment.startDate);
        setTherapy(oldTreatment.getTherapy());
        setPatientAddress(oldTreatment.getPatientAddress());
    }

    public Treatment updateForTransferIn(String tbId, String providerId, LocalDate startDate) {
        setTbId(tbId);
        setProviderId(providerId);
        setStartDate(startDate);
        return this;
    }

    public void close(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        endDate = dateModified.toLocalDate();
        this.treatmentOutcome = treatmentOutcome;
        therapy.close(dateModified);
    }

    public void pause(String reasonForPause, DateTime dateModified) {
        interruptions.add(new TreatmentInterruption(reasonForPause, dateModified.toLocalDate()));
    }

    public void resume(String reasonForResumption, DateTime dateModified) {
        interruptions.latestInterruption().resumeTreatment(reasonForResumption, dateModified.toLocalDate());
    }

    @JsonIgnore
    public Therapy getTherapy() {
        return therapy;
    }

    @JsonIgnore
    public void setTherapy(Therapy therapy) {
        this.therapy = therapy;
        this.therapyDocId = therapy.getId();
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
        return weightStatistics.isEmpty() || weightStatistics.latestResult().isValid(errorCodes);
    }

    private boolean areSmearInstancesValid(List<WHPErrorCode> errorCodes) {
        return smearTestResults.isEmpty() || smearTestResults.latestResult().isValid(errorCodes);
    }

    @JsonIgnore
    public boolean isClosed() {
        return therapy.isClosed();
    }

    public void addSmearTestResult(SmearTestRecord smearTestRecord) {
        smearTestResults.add(smearTestRecord);
    }

    public void addWeightStatistics(WeightStatisticsRecord weightStatisticsRecord) {
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

    public boolean isOnTreatment(LocalDate date) {
        if(endDate==null)
            return WHPDateUtil.isOnOrAfter(date,startDate);
        else
            return WHPDateUtil.isOnOrAfter(date,startDate) && WHPDateUtil.isOnOrBefore(date,endDate);
    }
}
