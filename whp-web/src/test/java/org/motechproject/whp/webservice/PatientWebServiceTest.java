package org.motechproject.whp.webservice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.application.service.RegistrationService;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.domain.Patient;
import org.motechproject.whp.repository.AllTreatments;
import org.motechproject.whp.request.PatientRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;


public class PatientWebServiceTest {

    @Mock
    AllTreatments allTreatments;
    @Mock
    RegistrationService patientRegistrationService;
    @Mock
    private RequestValidator validator;

    private PatientWebService patientWebService;

    @Before
    public void setUp() {
        initMocks(this);
        patientWebService = new PatientWebService(patientRegistrationService, allTreatments, validator);
    }

    @Test
    public void shouldCreatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();

        patientWebService.createCase(patientRequest);

        ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);
        verify(patientRegistrationService).registerPatient(patientArgumentCaptor.capture());
        verify(validator).validate(eq(patientRequest), eq(ValidationScope.create), eq("patient"));

        Patient patient = patientArgumentCaptor.getValue();
        assertEquals(patientRequest.getCase_id(), patient.getPatientId());
    }

    @Test(expected = NotImplementedException.class)
    public void shouldThrowExceptionOnUpdatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientWebService.updateCase(patientRequest);
    }
}
