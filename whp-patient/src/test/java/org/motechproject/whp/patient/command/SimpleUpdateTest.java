package org.motechproject.whp.patient.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.SmearTestRecord;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.TreatmentService;
import org.motechproject.whp.patient.service.treatmentupdate.BaseUnitTest;
import org.motechproject.whp.user.service.ProviderService;

import static org.joda.time.DateTime.now;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;

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

        expectWHPRuntimeException(WHPErrorCode.NO_SUCH_TREATMENT_EXISTS);
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

        expectWHPRuntimeException(WHPErrorCode.NO_SUCH_TREATMENT_EXISTS);
        simpleUpdate.apply(patientRequest);
        verify(allPatients, never()).update(patient);
    }

    @Test
    public void shouldNotPerformSimpleUpdateIfPatientIsNull() {
        PatientRequest patientRequest = new PatientRequest();
        //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        patientRequest.setCase_id("wrongCaseId");

        expectWHPRuntimeException(WHPErrorCode.INVALID_PATIENT_CASE_ID);
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

    @Test
    public void shouldPerformUpdateForSmearTestResults_forPatientWithClosedTreatment() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        SmearTestResults str = new SmearTestResults();
        str.add(new SmearTestRecord(SputumTrackingInstance.EndIP, today(), SmearTestResult.Negative, today(), SmearTestResult.Negative, "labName", "labNumber"));
        patientRequest.setSmearTestResults(str);
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");

        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        simpleUpdate.apply(patientRequest);
        verify(allPatients).update(patient);
    }

    @Test
    public void shouldPerformSimpleUpdateForEmptySmearTestResults() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .build();

        PatientRequest patientRequest = new PatientRequest();
        SmearTestResults str = new SmearTestResults();
        str.add(new SmearTestRecord(SputumTrackingInstance.PreTreatment, null, null, null, null, null, null));
        patientRequest.setSmearTestResults(str);
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");

        when(allPatients.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        simpleUpdate.apply(patientRequest);
        verify(allPatients).update(patient);
    }
}
