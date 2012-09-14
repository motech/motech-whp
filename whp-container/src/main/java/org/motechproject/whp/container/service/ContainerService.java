package org.motechproject.whp.container.service;

import freemarker.template.TemplateException;
import org.joda.time.DateTime;
import org.motechproject.whp.container.contract.RegistrationRequest;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.remedi.model.ContainerRegistrationModel;
import org.motechproject.whp.remedi.service.RemediService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ContainerService {

    private AllContainers allContainers;
    private RemediService remediService;

    @Autowired
    public ContainerService(AllContainers allContainers, RemediService remediService) {
        this.allContainers = allContainers;
        this.remediService = remediService;
    }

    public void registerContainer(RegistrationRequest registrationRequest) throws IOException, TemplateException {
        SputumTrackingInstance instance = SputumTrackingInstance.getInstanceForValue(registrationRequest.getInstance());
        Container container = new Container(registrationRequest.getProviderId().toLowerCase(), registrationRequest.getContainerId(), instance);
        allContainers.add(container);

        ContainerRegistrationModel containerRegistrationModel = new ContainerRegistrationModel(container.getContainerId(), container.getProviderId(), container.getInstance(), DateTime.now());
        remediService.sendContainerRegistrationResponse(containerRegistrationModel);
    }

    public Boolean exists(String containerId) {
        return allContainers.findByContainerId(containerId) != null;
    }
}
