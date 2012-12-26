package org.motechproject.whp.common.domain;

import lombok.Data;

@Data
public class ProviderPatientCount {
    private String providerId;
    private int patientCount;

    public ProviderPatientCount(String providerId, int patientCount) {
        this.providerId = providerId;
        this.patientCount = patientCount;
    }
}
