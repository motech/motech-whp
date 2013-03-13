package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.joda.time.LocalDate;

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
}
