package org.motechproject.whp.webservice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.request.PatientRequest;
import org.motechproject.whp.validation.ValidationScope;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.validation.Errors;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientWebServiceTest {

    @Mock
    AllPatients allPatients;
    @Mock
    AllTreatments allTreatments;
    @Mock
    private BeanValidator validator;

    private PatientWebService patientWebService;

    @Before
    public void setUp() {
        initMocks(this);
        patientWebService = new PatientWebService(allPatients, allTreatments, validator);
    }

    @Test
    public void shouldCreatePatient() throws WHPValidationException {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        when(allPatients.findByPatientId("caseId")).thenReturn(null);
        patientWebService.createCase(patientRequest);

        ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);

        verify(allPatients).add(patientArgumentCaptor.capture());
        verify(validator).validate(eq(patientRequest), eq(ValidationScope.create), Matchers.<Errors>any());

        Patient patient = patientArgumentCaptor.getValue();
        assertEquals(patientRequest.getCase_id(), patient.getPatientId());
    }

    @Test
    public void shouldUpdatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        Patient patientReturned = new Patient("1234567890", "fn", "ln", Gender.Male, PatientType.New, "87777987987");
        when(allPatients.findByPatientId("1234567890")).thenReturn(patientReturned);

        patientWebService.updateCase(patientRequest);

        ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);
        ArgumentCaptor<Patient> patientReturnedArgumentCaptor = ArgumentCaptor.forClass(Patient.class);

        verify(allPatients).update(patientReturnedArgumentCaptor.capture(), patientArgumentCaptor.capture());

        Patient patient = patientReturnedArgumentCaptor.getValue();
        assertEquals(patientReturned, patient);
    }
}
