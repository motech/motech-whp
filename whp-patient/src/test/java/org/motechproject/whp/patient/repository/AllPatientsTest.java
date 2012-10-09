package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.patient.WHPPatientConstants;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AllPatientsTest {
    @Mock
    private EventContext eventContext;

    @Mock
    private CouchDbConnector dbConnector;

    private AllPatients allPatients;

    @Before
    public void setup() {
        initMocks(this);
        allPatients = new AllPatients(dbConnector, eventContext);
    }

    @Test
    public void shouldRaiseEventWhenPatientIsUpdated() {
        Patient patient = new PatientBuilder().withDefaults().build();
        allPatients.update(patient);

        verify(eventContext).send(WHPPatientConstants.PATIENT_UPDATED_SUBJECT, patient);
    }

}
