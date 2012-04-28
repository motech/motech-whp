package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderControllerTest {

    ProviderController providerController;

    @Mock
    Model uiModel;
    @Mock
    private AllPatients allPatients;

    @Before
    public void setup() {
        initMocks(this);
        providerController = new ProviderController(allPatients);
    }

    @Test
    public void shouldListAllPatientsForProvider() {
        Patient patient = PatientBuilder.startRecording().withDefaults().build();
        List<Patient> patientsForProvider = Arrays.asList(patient);

        when(allPatients.findByCurrentProviderId("providerId")).thenReturn(patientsForProvider);

        providerController.list("providerId", uiModel);
        verify(uiModel).addAttribute(eq(ProviderController.PATIENT_LIST), same(patientsForProvider));
    }
}
