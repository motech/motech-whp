package org.motechproject.whp.container.domain;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class LabResults {
    private LocalDate smearTestDate1;
    private String smearTestResult1;
    private LocalDate smearTestDate2;
    private String smearTestResult2;
    private  String labName;
    private String labNumber;
}
