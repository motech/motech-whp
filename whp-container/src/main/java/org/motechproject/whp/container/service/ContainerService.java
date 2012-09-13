package org.motechproject.whp.container.service;

import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContainerService {

    private AllContainers allContainers;

    @Autowired
    public ContainerService(AllContainers allContainers) {
        this.allContainers = allContainers;
    }

    public void registerContainer(RegistrationRequest registrationRequest) {
        allContainers.add(new Container(registrationRequest.getProviderId().toLowerCase(), registrationRequest.getContainerId(), registrationRequest.getInstance()));
    }

    public Boolean exists(String containerId) {
        return allContainers.findByContainerId(containerId) != null;
    }
}
