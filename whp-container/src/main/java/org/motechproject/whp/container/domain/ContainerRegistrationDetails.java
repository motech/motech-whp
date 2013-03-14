package org.motechproject.whp.container.domain;

import lombok.Data;
import org.motechproject.whp.common.domain.Gender;

@Data
public class ContainerRegistrationDetails {
    private String patientName;
    private String patientId;
    private Integer patientAge;
    private Gender patientGender;

    public ContainerRegistrationDetails() {
    }

    public ContainerRegistrationDetails(String patientName, String patientId, Integer patientAge, Gender patientGender) {
        this.patientName = patientName;
        this.patientId = patientId;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
    }
}
