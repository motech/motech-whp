package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientControllerTest {

    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;
    @Mock
    AllPatients allPatients;
    PatientController providerController;
    PatientBuilder patientBuilder;

    @Before
    public void setup() {
        initMocks(this);
        patientBuilder = new PatientBuilder().withDefaults();
        providerController = new PatientController(allPatients);
    }

    @Test
    public void shouldListPatientsForProvider() {
        String view = providerController.listByProvider("providerId", uiModel, request);
        assertEquals("patient/listByProvider", view);
    }

    @Test
    public void shouldListAllPatientsForProvider() {
        List<Patient> patientsForProvider = asList(patientBuilder.build());
        when(allPatients.getAllWithActiveTreatmentFor("providerId")).thenReturn(patientsForProvider);

        providerController.listByProvider("providerId", uiModel, request);
        verify(uiModel).addAttribute(eq(PatientController.PATIENT_LIST), same(patientsForProvider));
    }

}
