package org.motechproject.whp.container.tracking.builder;


import org.motechproject.event.MotechEvent;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.patient.WHPPatientConstants;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.WHPUserConstants;
import org.motechproject.whp.user.domain.Provider;

public class DashboardEventsBuilder {

    public MotechEvent containerAddedEvent() {
        MotechEvent event = new MotechEvent(WHPContainerConstants.CONTAINER_ADDED_SUBJECT);
        event.getParameters().put("0", new Container());
        return event;
    }

    public MotechEvent providerUpdatedEvent() {
        MotechEvent event = new MotechEvent(WHPUserConstants.PROVIDER_UPDATED_SUBJECT);
        event.getParameters().put("0", new Provider());
        return event;
    }

    public MotechEvent patientUpdatedEvent() {
        MotechEvent event = new MotechEvent(WHPPatientConstants.PATIENT_UPDATED_SUBJECT);
        event.getParameters().put("0", new Patient());
        return event;
    }
}
