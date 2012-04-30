package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@Data
@TypeDiscriminator("doc.type == 'TreatmentCategory'")
public class TreatmentCategory extends MotechBaseDataObject {

    private String name;
    private String code;
    private Integer dosesPerWeek;
    private Integer numberOfWeeksOfIP;
    private Integer numberOfWeeksOfCP;

    public TreatmentCategory() {
    }

    public TreatmentCategory(String name, String code, Integer dosesPerWeek, Integer numberOfWeeksOfIP, Integer numberOfWeeksOfCP) {
        this.name = name;
        this.code = code;
        this.dosesPerWeek = dosesPerWeek;
        this.numberOfWeeksOfIP = numberOfWeeksOfIP;
        this.numberOfWeeksOfCP = numberOfWeeksOfCP;
    }
}
