package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.EventRelay;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.patient.WHPPatientConstants;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AllPatientsTest {
    @Mock
    private EventRelay eventRelay;

    @Mock
    private CouchDbConnector dbConnector;

    private AllPatients allPatients;

    @Before
    public void setup() {
        initMocks(this);
        allPatients = new AllPatients(dbConnector, eventRelay);
    }

    @Test
    public void shouldRaiseEventWhenPatientIsUpdated() {
        Patient patient = new PatientBuilder().withDefaults().build();
        allPatients.update(patient);

        assertTrue(eventRaisedWithSubject(WHPPatientConstants.PATIENT_UPDATED_SUBJECT));
        assertTrue(eventRaisedWithParameter(WHPPatientConstants.PATIENT_KEY, patient));
    }

    private boolean eventRaisedWithSubject(String patientUpdatedSubject) {
        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(captor.capture());
        return patientUpdatedSubject.equals(captor.getValue().getSubject());
    }

    private boolean eventRaisedWithParameter(String key, Object value) {
        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(captor.capture());
        return value.equals(captor.getValue().getParameters().get(key));
    }
}
