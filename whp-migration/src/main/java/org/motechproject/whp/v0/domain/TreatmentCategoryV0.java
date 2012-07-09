package org.motechproject.whp.v0.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.DayOfWeek;
import org.motechproject.model.MotechBaseDataObject;

import java.util.List;

@Data
@TypeDiscriminator("doc.type == 'TreatmentCategory'")
public class TreatmentCategoryV0 extends MotechBaseDataObject {

    private String name;
    private String code;

    private Integer dosesPerWeek;

    private Integer numberOfDosesInIP;
    private Integer numberOfWeeksOfIP;

    private Integer numberOfWeeksOfEIP;
    private Integer numberOfDosesInEIP;

    private Integer numberOfWeeksOfCP;
    private Integer numberOfDosesInCP;

    private List<DayOfWeek> pillDays;

    public TreatmentCategoryV0() {
    }

    public TreatmentCategoryV0(String name, String code, Integer dosesPerWeek,
                               Integer numberOfWeeksOfIP, Integer numberOfDosesInIP,
                               Integer numberOfWeeksOfEIP, Integer numberOfDosesInEIP,
                               Integer numberOfWeeksOfCP, Integer numberOfDosesInCP,
                               List<DayOfWeek> pillDays) {
        this.name = name;
        this.code = code;
        this.dosesPerWeek = dosesPerWeek;
        this.numberOfWeeksOfIP = numberOfWeeksOfIP;
        this.numberOfDosesInIP = numberOfDosesInIP;
        this.numberOfWeeksOfEIP = numberOfWeeksOfEIP;
        this.numberOfDosesInEIP = numberOfDosesInEIP;
        this.numberOfWeeksOfCP = numberOfWeeksOfCP;
        this.numberOfDosesInCP = numberOfDosesInCP;
        this.pillDays = pillDays;
    }
}