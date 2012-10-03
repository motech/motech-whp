package org.motechproject.whp.container.service;

import freemarker.template.TemplateException;
import org.joda.time.DateTime;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.remedi.model.ContainerRegistrationModel;
import org.motechproject.whp.remedi.service.RemediService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ContainerService {

    private AllContainers allContainers;
    private RemediService remediService;

    @Autowired
    public ContainerService(AllContainers allContainers, RemediService remediService) {
        this.allContainers = allContainers;
        this.remediService = remediService;
    }

    public void registerContainer(ContainerRegistrationRequest registrationRequest) throws IOException, TemplateException {
        SputumTrackingInstance instance = SputumTrackingInstance.getInstanceForValue(registrationRequest.getInstance());
        DateTime creationTime = DateUtil.now();

        Container container = new Container(registrationRequest.getProviderId().toLowerCase(), registrationRequest.getContainerId(), instance, creationTime);
        allContainers.add(container);

        ContainerRegistrationModel containerRegistrationModel = new ContainerRegistrationModel(container.getContainerId(), container.getProviderId(), container.getInstance(), creationTime);
        remediService.sendContainerRegistrationResponse(containerRegistrationModel);
    }

    public boolean exists(String containerId) {
        return allContainers.findByContainerId(containerId) != null;
    }

    public Container getContainer(String containerId) {
        return allContainers.findByContainerId(containerId);
    }

    public void update(Container container) {
        allContainers.update(container);
    }

    public List<Container> findByPatientId(String patientId) {
        return allContainers.findByPatientId(patientId);
    }
}
