package org.motechproject.whp.uimodel;

import lombok.Data;

@Data
public class ContainerTrackingDashboardRow {

    private String containerId;
    private String containerIssuedOn;
    private String consultationOneDate;
    private String consultationTwoDate;
    private String consultationOneResult;
    private String consultationTwoResult;
    private String consultation;
    private String diagnosis;
    private String patientId;
    private String district;
    private String providerId;
    private String labName;
    private String containerStatus;
    private String reasonForClosure;
}
