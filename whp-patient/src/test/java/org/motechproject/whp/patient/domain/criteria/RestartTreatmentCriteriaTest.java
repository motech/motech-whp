package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canRestartCurrentTreatment;

public class RestartTreatmentCriteriaTest {

     ArrayList<WHPErrorCode> errorCodes;

    public RestartTreatmentCriteriaTest() {
        initMocks(this);
         errorCodes = new ArrayList<WHPErrorCode>();
    }

    @Test
    public void shouldReturnFalseForCanRestartCurrentTreatmentIfPatientTreatmentIsNotPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);

        assertFalse(canRestartCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_IN_PROGRESS));
    }

    @Test
    public void shouldReturnFalseForCanRestartCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");

        assertFalse(canRestartCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
    }

    @Test
    public void shouldReturnFalseForCanRestartCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId_AndPatientTreatmentIsNotPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");

        assertFalse(canRestartCurrentTreatment(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_ALREADY_IN_PROGRESS));
    }

    @Test
    public void shouldReturnTrueForCanRestartCurrentTreatmentIfPatientTreatmentCanBeRestarted() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id(tbId);

        assertTrue(canRestartCurrentTreatment(patient, patientRequest, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }
}
