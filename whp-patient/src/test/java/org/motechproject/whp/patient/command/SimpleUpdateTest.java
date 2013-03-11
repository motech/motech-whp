package org.motechproject.whp.patient.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.patient.service.TreatmentService;
import org.motechproject.whp.patient.service.treatmentupdate.BaseUnitTest;
import org.motechproject.whp.user.service.ProviderService;

import static org.joda.time.DateTime.now;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;

public class SimpleUpdateTest extends BaseUnitTest {

    @Mock
    private PatientService patientService;
    @Mock
    private ProviderService providerService;
    @Mock
    private TreatmentService treatmentService;
    @Mock
    private AllDistricts allDistricts;
    private SimpleUpdate simpleUpdate;
    private Patient patient;
    private PatientMapper patientMapper;

    @Before
    public void setUp() {
        initMocks(this);
        patientMapper = new PatientMapper(providerService);
        patient = new PatientBuilder().withDefaults().build();
        simpleUpdate = new SimpleUpdate(patientService, patientMapper, allDistricts);
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
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.NO_SUCH_TREATMENT_EXISTS);
        simpleUpdate.apply(patientRequest);
        verify(patientService, never()).update(patient);
    }

    @Test
    public void shouldNotPerformSimpleUpdateIfPatientTbIdDoesNotMatchUpdateRequestTbId() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("tbId")
                .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.NO_SUCH_TREATMENT_EXISTS);
        simpleUpdate.apply(patientRequest);
        verify(patientService, never()).update(patient);
    }

    @Test
    public void shouldNotPerformSimpleUpdateIfPatientIsNull() {
        PatientRequest patientRequest = new PatientRequest();
        //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        patientRequest.setCase_id("wrongCaseId");

        expectWHPRuntimeException(WHPErrorCode.INVALID_PATIENT_CASE_ID);
        simpleUpdate.apply(patientRequest);
        verify(patientService, never()).update(patient);
    }

    @Test
    public void shouldNotPerformSimpleUpdateIfDistrictIsInvalid() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");
        String invalid_district = "invalid_district";
        patientRequest.setAddress(new Address("", "", "", "", invalid_district, ""));
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);
        when(allDistricts.findByName(invalid_district)).thenReturn(null);

        expectWHPRuntimeException(WHPErrorCode.INVALID_DISTRICT);
        simpleUpdate.apply(patientRequest);
        verify(patientService, never()).update(patient);
    }

    @Test
    public void shouldPerformSimpleUpdateIfAddressIsNotGiven() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");
        patientRequest.setAddress(new Address(null, null, null, null, null, null));
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        simpleUpdate.apply(patientRequest);
        verify(allDistricts, never()).findByName(anyString());
    }

    @Test
    public void shouldNotPerformSimpleUpdateIfDistrictIsNotGiven() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");
        patientRequest.setAddress(new Address("", "", "", "", null, ""));
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);

        expectWHPRuntimeException(WHPErrorCode.INVALID_DISTRICT);
        simpleUpdate.apply(patientRequest);
        verify(patientService, never()).update(patient);
    }

    @Test
    public void shouldPerformSimpleUpdate() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");
        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);
        when(allDistricts.findByName(anyString())).thenReturn(new District());


        simpleUpdate.apply(patientRequest);
        verify(patientService).update(patient);
    }

    @Test
    public void shouldPerformUpdateForSmearTestResults_forPatientWithClosedTreatment() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, null, now());

        PatientRequest patientRequest = new PatientRequest();
        SmearTestResults str = new SmearTestResults();
        str.add(new SmearTestRecord(SputumTrackingInstance.EndIP, today(), SmearTestResult.Negative, today(), SmearTestResult.Negative, "labName", "labNumber"));
        patientRequest.setSmearTestResults(str);
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");

        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);
        when(allDistricts.findByName(anyString())).thenReturn(new District());

        simpleUpdate.apply(patientRequest);
        verify(patientService).update(patient);
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

        when(patientService.findByPatientId(patientRequest.getCase_id())).thenReturn(patient);
        when(allDistricts.findByName(anyString())).thenReturn(new District());

        simpleUpdate.apply(patientRequest);
        verify(patientService).update(patient);
    }
}
