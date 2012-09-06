package org.motechproject.whp.patient.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.TreatmentService;
import org.motechproject.whp.patient.service.treatmentupdate.BaseUnitTest;
import org.motechproject.whp.user.service.ProviderService;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SimpleUpdateTest extends BaseUnitTest {

    @Mock
    private AllPatients allPatients;
    @Mock
    private ProviderService providerService;
    @Mock
    private TreatmentService treatmentService;
    private SimpleUpdate simpleUpdate;
    private Patient patient;
    private PatientMapper patientMapper;
    @Before
    public void setUp() {
        initMocks(this);
        patientMapper = new PatientMapper(providerService);
        patient = new PatientBuilder().withDefaults().build();
        simpleUpdate = new SimpleUpdate(allPatients, patientMapper);
    }

    @Test
    public void shouldNotPerformSimpleUpdateIfPatientDoesNotHaveEvenASingleTreatment() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .withCurrentTreatment(null)
                .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE);
        simpleUpdate.apply(patientRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldNotPerformSimpleUpdateIfPatientTbIdDoesNotMatchUpdateRequestTbId() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("tbId")
                .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.TB_ID_DOES_NOT_MATCH);
        simpleUpdate.apply(patientRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldNotPerformSimpleUpdateIfPatientIsNull() {
        PatientRequest patientRequest = new PatientRequest();
        //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        patientRequest.setCase_id("wrongCaseId");

        expectWHPRuntimeException(WHPErrorCode.CASE_ID_DOES_NOT_EXIST);
        simpleUpdate.apply(patientRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldPerformSimpleUpdate() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");
        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);


        simpleUpdate.apply(patientRequest);
        verify(allPatients).update(patient);
    }
}
