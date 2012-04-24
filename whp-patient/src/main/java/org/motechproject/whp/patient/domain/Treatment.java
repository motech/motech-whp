package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;

@Data
@TypeDiscriminator("doc.type == 'Treatment'")
public class Treatment extends MotechBaseDataObject {

    private int patientAge;
    private Category category;
    private LocalDate startDate;
    private LocalDate endDate;
    private String tbRegistrationNumber;
    private DiseaseClass diseaseClass;
    private List<SmearTestResults> smearTestResults = new ArrayList<SmearTestResults>();
    private List<WeightStatistics> weightStatisticsList = new ArrayList<WeightStatistics>();

    public Treatment() {
    }

    public Treatment(Category category, LocalDate startDate, DiseaseClass diseaseClass, int patientAge) {
        this.category = category;
        this.startDate = startDate;
        this.diseaseClass = diseaseClass;
        this.patientAge = patientAge;
    }

    public Treatment setRegistrationDetails(String registrationNumber) {
        this.setTbRegistrationNumber(registrationNumber);
        return this;
    }

    public void addSmearTestResult(SmearTestResults smearTestResults) {
        this.smearTestResults.add(smearTestResults);
    }

    public void addWeightStatistics(WeightStatistics weightStatistics) {
        weightStatisticsList.add(weightStatistics);
    }

}
