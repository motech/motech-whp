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
    private List<SmearTestResult> smearTestResults;

    public Treatment() {
    }

    public Treatment(Category category, LocalDate startDate) {
        smearTestResults = new ArrayList<SmearTestResult>();
        this.category = category;
        this.startDate = startDate;
    }

    public Treatment setRegistrationDetails(String registrationNumber, LocalDate registrationDate) {
        this.setRegistrationNumber(registrationNumber);
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void addSmearTestResult(SmearTestResult smearTestResult){
       smearTestResults.add(smearTestResult);
    }

}
