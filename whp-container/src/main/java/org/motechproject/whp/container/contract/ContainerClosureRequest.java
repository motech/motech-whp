package org.motechproject.whp.container.contract;

import lombok.Data;

@Data
public class ContainerClosureRequest {

    String containerId;

    String reason;

    String alternateDiagnosis;

    String consultationDate;

}
