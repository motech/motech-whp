package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@TypeDiscriminator("doc.type == 'Treatment'")
public class Treatment extends MotechBaseDataObject {

    private Integer patientAge;
    private TreatmentCategory treatmentCategory;
    private LocalDate startDate;
    private DateTime creationDate;
    private LocalDate closeDate;
    private String tbRegistrationNumber;
    private DiseaseClass diseaseClass;
    private SmearTestInstances smearTestInstances = new SmearTestInstances();
    private WeightInstances weightInstances = new WeightInstances();
    private TreatmentInterruptions interruptions = new TreatmentInterruptions();

    // Required for ektorp
    public Treatment() {
    }

    public Treatment(TreatmentCategory treatmentCategory, DiseaseClass diseaseClass, Integer patientAge) {
        this.treatmentCategory = treatmentCategory;
        this.diseaseClass = diseaseClass;
        this.patientAge = patientAge;
    }

    public void addSmearTestResult(SmearTestResults smearTestResults) {
        smearTestInstances.add(smearTestResults);
    }

    public void addWeightStatistics(WeightStatistics weightStatistics) {
        weightInstances.add(weightStatistics);
    }

    public void close(DateTime dateModified) {
        closeDate = dateModified.toLocalDate();
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> errorCodes) {
        boolean isLatestSmearResultValid = true;
        boolean isLatestWeightStatisticValid = true;
        if(!smearTestInstances.isEmpty()) {
            isLatestSmearResultValid = smearTestInstances.latestResult().isValid(errorCodes);
        }
        if(!CollectionUtils.isEmpty(weightInstances)) {
            isLatestWeightStatisticValid = weightInstances.latestResult().isValid(errorCodes);
        }
        return isLatestSmearResultValid && isLatestWeightStatisticValid;
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

}
