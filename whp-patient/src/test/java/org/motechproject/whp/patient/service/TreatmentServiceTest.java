package org.motechproject.whp.patient.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.service.ProviderService;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.patient.builder.PatientBuilder.PATIENT_ID;

public class TreatmentServiceTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private ProviderService providerService;

    private TreatmentService treatmentService;

    private PatientMapper patientMapper;

    @Before
    public void setUp() {
        initMocks(this);
        patientMapper = new PatientMapper(providerService);
        Patient patient = new PatientBuilder().withDefaults().build();
        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);

        treatmentService = new TreatmentService(allPatients, patientMapper);
    }

    @Test
    public void shouldOverwriteDiseaseClass_DuringTransferIn() {
        String providerId = "provider-id";
        PatientRequest transferInRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withProviderId(providerId)
                .withDiseaseClass(DiseaseClass.E)
                .build();

        when(providerService.findByProviderId(providerId))
                .thenReturn(new ProviderBuilder()
                        .withProviderId(providerId)
                        .withDistrict("district").build());

        treatmentService.transferInPatient(transferInRequest);

        ArgumentCaptor<Patient> patientArgumentCaptor = ArgumentCaptor.forClass(Patient.class);
        verify(allPatients).update(patientArgumentCaptor.capture());
        assertEquals(DiseaseClass.E, patientArgumentCaptor.getValue().getCurrentTherapy().getDiseaseClass());
    }

}
