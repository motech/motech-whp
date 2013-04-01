package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.util.WHPDate;

import java.io.Serializable;

@Data
public class TreatmentDetails implements Serializable {

    private String districtWithCode;
    private String tbUnitWithCode;
    private String epSite;
    private String otherInvestigations;
    private String previousTreatmentHistory;
    private String hivStatus;
    private LocalDate hivTestDate;
    private Integer membersBelowSixYears;
    private String phcReferred;
    private String providerName;
    private String dotCentre;
    private String providerType;
    private String cmfDoctor;
    private String contactPersonName;
    private String contactPersonPhoneNumber;
    private String xpertTestResult;
    private String xpertDeviceNumber;
    private LocalDate xpertTestDate;
    private String rifResistanceResult;


    @JsonIgnore
    public String getXpertTestDateInDesiredFormat() {
        if(xpertTestDate != null)
            return WHPDate.date(xpertTestDate).value();
        return "";
    }

    @JsonIgnore
    public String getHivTestDateInDesiredFormat() {
        if(hivTestDate != null)
            return WHPDate.date(hivTestDate).value();
        return "";
    }
}
