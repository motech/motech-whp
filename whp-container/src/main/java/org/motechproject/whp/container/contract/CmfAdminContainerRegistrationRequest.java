package org.motechproject.whp.container.contract;

import lombok.Data;
import org.motechproject.whp.common.domain.ChannelId;

@Data
public class CmfAdminContainerRegistrationRequest extends ContainerRegistrationRequest {
    private ContainerRegistrationMode containerRegistrationMode;

    public CmfAdminContainerRegistrationRequest() {
    }

    public CmfAdminContainerRegistrationRequest(String providerId, String containerId, String instance, ContainerRegistrationMode containerRegistrationMode, ChannelId channelId, String callId) {
        super(providerId, containerId, instance, channelId.name(), callId);
        this.containerRegistrationMode = containerRegistrationMode;
    }
}
