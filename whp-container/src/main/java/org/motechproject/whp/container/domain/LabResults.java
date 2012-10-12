package org.motechproject.whp.container.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.SmearTestResult;

import java.io.Serializable;

@Data
public class LabResults implements Serializable {
    private LocalDate smearTestDate1;
    private SmearTestResult smearTestResult1;
    private LocalDate smearTestDate2;
    private SmearTestResult smearTestResult2;
    private SmearTestResult cumulativeResult;
    private  String labName;
    private String labNumber;

    public void updateCumulativeResult() {
        cumulativeResult = smearTestResult1.cumulativeResult(smearTestResult2);
    }
}
