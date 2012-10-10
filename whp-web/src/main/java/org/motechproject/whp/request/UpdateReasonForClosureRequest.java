package org.motechproject.whp.request;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UpdateReasonForClosureRequest {

    String containerId;

    @Size(min = 1, message = "Reason For Closure Cannot be empty")
    String reason;

    String alternateDiagnosisForTBNegative;

    String date;

}
