package org.motechproject.whp.containertracking.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.containertracking.service.ContainerTrackingService;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.patient.WHPPatientConstants;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.WHPUserConstants;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContainerTrackingEventHandler {

    private ContainerTrackingService containerTrackingService;

    @Autowired
    public ContainerTrackingEventHandler(ContainerTrackingService containerTrackingService) {
        this.containerTrackingService = containerTrackingService;
    }

    @MotechListener(subjects = WHPContainerConstants.CONTAINER_ADDED_SUBJECT)
    public void onContainerAdded(MotechEvent event) {
        Container container = (Container) event.getParameters().get("0");
        containerTrackingService.createDashboardRow(container);
    }

    @MotechListener(subjects = WHPContainerConstants.CONTAINER_UPDATED_SUBJECT)
    public void onContainerUpdated(MotechEvent event) {
        Container container = (Container) event.getParameters().get("0");
        containerTrackingService.updateDashboardRow(container);
    }

    @MotechListener(subjects = WHPUserConstants.PROVIDER_UPDATED_SUBJECT)
    public void onProviderUpdated(MotechEvent event) {
        Provider provider = (Provider) event.getParameters().get("0");
        containerTrackingService.updateProviderInformation(provider);
    }

    @MotechListener(subjects = WHPPatientConstants.PATIENT_UPDATED_SUBJECT)
    public void onPatientUpdated(MotechEvent event) {
        Patient patient = (Patient) event.getParameters().get("0");
        containerTrackingService.updatePatientInformation(patient);
    }
}
