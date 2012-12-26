package org.motechproject.whp.applicationservice.adherence;

import lombok.Data;

@Data
public class ProviderAdherenceStatus {
    private String providerId;
    private int patientCount;
    private int patientWithAdherenceCount;

    public ProviderAdherenceStatus(String providerId, int patientCount) {
        this.providerId = providerId;
        this.patientCount = patientCount;
    }

    public boolean isAdherencePending() {
        return patientCount > patientWithAdherenceCount;
    }
}
