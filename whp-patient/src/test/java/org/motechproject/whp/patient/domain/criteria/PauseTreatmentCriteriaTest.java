package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canPauseCurrentTreatment;

public class PauseTreatmentCriteriaTest {

    List<WHPErrorCode> errorCodes;

    public PauseTreatmentCriteriaTest() {
        initMocks(this);
        errorCodes = new ArrayList<WHPErrorCode>();
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfPatientTreatmentIsAlreadyPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);

        assertFalse(canPauseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_PAUSED));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId_AndPatientTreatmentIsAlreadyPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_PAUSED));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfCurrentTreatmentIsClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);

        assertFalse(canPauseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId_AndPatientTreatmentIsClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId_AndPatientTreatmentIsClosed_AndPatientTreatmentIsPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfPatientTreatmentIsPaused_AndPatientTreatmentIsClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);

        assertFalse(canPauseCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnTrueForCanPauseCurrentTreatmentIfPatientTreatmentCanBePaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);

        assertTrue(canPauseCurrentTreatment(patient, patientRequest, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }
}
