package org.motechproject.whp.container.contract;

import lombok.Data;

@Data
public class CmfAdminContainerRegistrationRequest extends ContainerRegistrationRequest {
    private ContainerRegistrationMode containerRegistrationMode;

    public CmfAdminContainerRegistrationRequest() {
    }

    public CmfAdminContainerRegistrationRequest(String providerId, String containerId, String instance, ContainerRegistrationMode containerRegistrationMode) {
        super(providerId, containerId, instance);
        this.containerRegistrationMode = containerRegistrationMode;
    }
}
