package org.motechproject.whp.container.contract;

import lombok.Data;

@Data
public class UpdateReasonForClosureRequest {

    String containerId;

    String reason;

    String alternateDiagnosis;

    String consultationDate;

}
