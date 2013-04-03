package org.motechproject.whp.container.contract;

import lombok.Data;

@Data
public class ContainerPatientDetailsRequest {

    String containerId;

    private String patientName;

    private String patientId;

}
