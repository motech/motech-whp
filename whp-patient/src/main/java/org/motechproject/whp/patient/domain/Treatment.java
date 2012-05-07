package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.whp.patient.repository.ValidationErrors;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@TypeDiscriminator("doc.type == 'Treatment'")
public class Treatment extends MotechBaseDataObject {

    private Integer patientAge;
    private TreatmentCategory treatmentCategory;
    private LocalDate doseStartDate;
    private DateTime startDate;
    private LocalDate endDate;
    private String tbRegistrationNumber;
    private String treatmentComplete;
    private String reasonForClosure;
    private DiseaseClass diseaseClass;
    private List<SmearTestResults> smearTestResults = new ArrayList<SmearTestResults>();
    private List<WeightStatistics> weightStatisticsList = new ArrayList<WeightStatistics>();

    // Required for ektorp
    public Treatment() {
    }

    public Treatment(TreatmentCategory treatmentCategory, DiseaseClass diseaseClass, int patientAge) {
        this.treatmentCategory = treatmentCategory;
        this.diseaseClass = diseaseClass;
        this.patientAge = patientAge;
    }

    public void addSmearTestResult(SmearTestResults smearTestResults) {
        this.smearTestResults.add(smearTestResults);
    }

    public void addWeightStatistics(WeightStatistics weightStatistics) {
        weightStatisticsList.add(weightStatistics);
    }

    public SmearTestResults latestSmearTestResult() {
        return smearTestResults.get(smearTestResults.size() - 1);
    }

    public WeightStatistics latestWeightStatistics() {
        return weightStatisticsList.get(weightStatisticsList.size() - 1);
    }

    @JsonIgnore
    public boolean isValid(ValidationErrors validationErrors) {
        boolean isLatestSmearResultValid = true;
        boolean isLatestWeightStatisticValid = true;
        if(!CollectionUtils.isEmpty(smearTestResults)) {
            isLatestSmearResultValid = latestSmearTestResult().isValid(validationErrors);
        }
        if(!CollectionUtils.isEmpty(weightStatisticsList)) {
            isLatestWeightStatisticValid = latestWeightStatistics().isValid(validationErrors);
        }
        return isLatestSmearResultValid && isLatestWeightStatisticValid;
    }
}
