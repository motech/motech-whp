package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.*;

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

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_PAUSED));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId_AndPatientTreatmentIsAlreadyPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_PAUSED));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfCurrentTreatmentIsClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment("Cured", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId_AndPatientTreatmentIsClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment("Cured", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId_AndPatientTreatmentIsClosed_AndPatientTreatmentIsPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());
        patient.closeCurrentTreatment("Cured", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfPatientTreatmentIsPaused_AndPatientTreatmentIsClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());
        patient.closeCurrentTreatment("Cured", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnTrueForCanPauseCurrentTreatmentIfPatientTreatmentCanBePaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertTrue(canPauseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }
}
