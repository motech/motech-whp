package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@TypeDiscriminator("doc.type == 'Treatment'")
public class Treatment extends MotechBaseDataObject {

    @NotNull
    private String id;
    @NotNull
    private Category category;
    private LocalDate startDate;
    private LocalDate endDate;
    private String registrationNumber;
    private LocalDate registrationDate;
    private DiseaseClass diseaseClass;
    private List<SmearTestResult> smearTestResults = new ArrayList<SmearTestResult>();
    private List<WeightStatistics> weightStatisticsList = new ArrayList<WeightStatistics>();

    public Treatment() {
    }

    public Treatment(Category category, LocalDate startDate, DiseaseClass diseaseClass) {
        this.category = category;
        this.startDate = startDate;
        this.diseaseClass = diseaseClass;
    }

    public Treatment setRegistrationDetails(String registrationNumber, LocalDate registrationDate) {
        this.setRegistrationNumber(registrationNumber);
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
