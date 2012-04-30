package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.DayOfWeek;
import org.motechproject.model.MotechBaseDataObject;

import java.util.List;

@Data
@TypeDiscriminator("doc.type == 'TreatmentCategory'")
public class TreatmentCategory extends MotechBaseDataObject {

    private String name;
    private String code;
    private Integer dosesPerWeek;
    private Integer numberOfWeeksOfIP;
    private Integer numberOfWeeksOfCP;
    private List<DayOfWeek> pillDays;

    public TreatmentCategory() {
    }

    public TreatmentCategory(String name, String code, Integer dosesPerWeek,
                             Integer numberOfWeeksOfIP, Integer numberOfWeeksOfCP,
                             List<DayOfWeek> pillDays) {
        this.name = name;
        this.code = code;
        this.dosesPerWeek = dosesPerWeek;
        this.numberOfWeeksOfIP = numberOfWeeksOfIP;
        this.numberOfWeeksOfCP = numberOfWeeksOfCP;
        this.pillDays = pillDays;
    }
}
