package org.motechproject.whp.container.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.SmearTestResult;

@Data
public class LabResults {
    private LocalDate smearTestDate1;
    private SmearTestResult smearTestResult1;
    private LocalDate smearTestDate2;
    private SmearTestResult smearTestResult2;
    private  String labName;
    private String labNumber;
}
