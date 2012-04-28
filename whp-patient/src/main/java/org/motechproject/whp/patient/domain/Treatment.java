package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.whp.patient.repository.ValidationErrors;

import java.util.ArrayList;
import java.util.List;

@Data
@TypeDiscriminator("doc.type == 'Treatment'")
public class Treatment extends MotechBaseDataObject {

    private int patientAge;
    private TreatmentCategory treatmentCategory;
    private LocalDate doseStartDate;
    private DateTime startDate;
    private LocalDate endDate;
    private String tbRegistrationNumber;
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

    @JsonIgnore
    public String getStartDateAsDate() {
        return startDate.toLocalDate().toString("dd/MM/YYYY");
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
        return latestSmearTestResult().isValid(validationErrors) && latestWeightStatistics().isValid(validationErrors);
    }

}
