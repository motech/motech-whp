package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.errorcode.WHPDomainErrorCode;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canRestartCurrentTreatment;

public class RestartTreatmentCriteriaTest {

     ArrayList<WHPDomainErrorCode> errorCodes;

    public RestartTreatmentCriteriaTest() {
        initMocks(this);
         errorCodes = new ArrayList<WHPDomainErrorCode>();
    }

    @Test
    public void shouldReturnFalseForCanRestartCurrentTreatmentIfPatientTreatmentIsNotPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertFalse(canRestartCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TREATMENT_ALREADY_IN_PROGRESS));
    }

    @Test
    public void shouldReturnFalseForCanRestartCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id("wrongTbId");

        assertFalse(canRestartCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TB_ID_DOES_NOT_MATCH));
    }

    @Test
    public void shouldReturnFalseForCanRestartCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId_AndPatientTreatmentIsNotPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id("wrongTbId");

        assertFalse(canRestartCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TB_ID_DOES_NOT_MATCH));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TREATMENT_ALREADY_IN_PROGRESS));
    }

    @Test
    public void shouldReturnTrueForCanRestartCurrentTreatmentIfPatientTreatmentCanBeRestarted() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertTrue(canRestartCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }
}
