package org.motechproject.whp.container.contract;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String containerId;

    private String instance;

    private String providerId;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String providerId, String containerId, String instance) {
        this.providerId = providerId;
        this.containerId = containerId;
        this.instance = instance;
    }
}
