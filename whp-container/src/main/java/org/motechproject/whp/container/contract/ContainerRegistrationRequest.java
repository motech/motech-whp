package org.motechproject.whp.container.contract;

import lombok.Data;
import org.motechproject.whp.common.domain.Gender;
import org.motechproject.whp.container.domain.ContainerRegistrationMode;

import static org.motechproject.whp.container.domain.ContainerRegistrationMode.ON_BEHALF_OF_PROVIDER;

@Data
public class ContainerRegistrationRequest {
    private String containerId;

    private String instance;

    private String providerId;

    private String channelId;
    private String callId;
    private String submitterId;
    private String submitterRole;
    private ContainerRegistrationMode containerRegistrationMode = ON_BEHALF_OF_PROVIDER;

    private String patientName;
    private Integer age;
    private Gender gender;
    private String patientId;

    public ContainerRegistrationRequest() {
    }

    public ContainerRegistrationRequest(String providerId, String containerId, String instance, String channelId, String callId) {
        this.providerId = providerId;
        this.containerId = containerId;
        this.instance = instance;
        this.channelId = channelId;
        this.callId = callId;
    }

}
