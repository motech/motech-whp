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
    private LocalDate registrationDate;
    private DiseaseClass diseaseClass;
    private List<SmearTestResult> smearTestResults = new ArrayList<SmearTestResult>();
    private List<WeightStatistics> weightStatisticsList = new ArrayList<WeightStatistics>();

    public Treatment() {
    }

    public Treatment(Category category, LocalDate startDate, DiseaseClass diseaseClass, int patientAge) {
        this.category = category;
        this.startDate = startDate;
        this.diseaseClass = diseaseClass;
        this.patientAge = patientAge;
    }

    public Treatment setRegistrationDetails(String registrationNumber, LocalDate registrationDate) {
        this.setTbRegistrationNumber(registrationNumber);
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void addSmearTestResult(SmearTestResult smearTestResult) {
        smearTestResults.add(smearTestResult);
    }

    public void addWeightStatistics(WeightStatistics weightStatistics) {
        weightStatisticsList.add(weightStatistics);
    }

}
