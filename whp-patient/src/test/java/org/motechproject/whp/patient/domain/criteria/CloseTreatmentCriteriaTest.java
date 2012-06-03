package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canCloseCurrentTreatment;

public class CloseTreatmentCriteriaTest {

     ArrayList<WHPErrorCode> errorCodes;

    public CloseTreatmentCriteriaTest() {
        initMocks(this);
        errorCodes = new ArrayList<WHPErrorCode>();
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());


        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);

        assertFalse(canCloseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentTbIdDoesNotMatchRequestTbId() {
        String tbId = "tbId";
        String someOtherTbId = "someOtherTbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(someOtherTbId);

        assertFalse(canCloseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentTbIdDoesNotMatchRequestTbIdAndTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        String someOtherTbId = "someOtherTbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(someOtherTbId);

        assertFalse(canCloseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatment(null).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);

        assertFalse(canCloseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE));
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientIsNull() {
        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id("caseId"); //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        patientRequest.setTb_id("tbId");

        assertFalse(canCloseCurrentTreatment(null, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.CASE_ID_DOES_NOT_EXIST));
    }

    @Test
    public void shouldReturnTrueForCanCloseCurrentTreatmentIfPatientTreatmentCanBeClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);

        assertTrue(canCloseCurrentTreatment(patient, patientRequest, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }
}
