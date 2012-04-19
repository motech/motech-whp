package org.motechproject.whp.webservice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.request.PatientRequest;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientWebServiceTest {

    @Mock
    AllPatients allPatients;
    @Mock
    AllTreatments allTreatments;

    PatientWebService patientWebService;

    @Before
    public void setUp() {
        initMocks(this);
        patientWebService = new PatientWebService(allPatients, allTreatments);
    }

    @Test
    public void shouldCreatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        when(allPatients.findByPatientId("caseId")).thenReturn(null);
        patientWebService.createCase(patientRequest);

        ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);

        verify(allPatients).add(patientArgumentCaptor.capture());

        Patient patient = patientArgumentCaptor.getValue();
        assertEquals("caseId", patient.getPatientId());
    }
}
