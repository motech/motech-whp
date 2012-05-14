package org.motechproject.whp.patient.domain.criteria;

import junit.framework.Assert;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.*;

public class PauseTreatmentCriteriaTest {

    CriteriaErrors criteriaErrors;

    public PauseTreatmentCriteriaTest() {
        initMocks(this);
        criteriaErrors = new CriteriaErrors();
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfPatientTreatmentIsAlreadyPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Current treatment is already paused"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"No such tb id for current treatment"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanPauseCurrentTreatmentIfRequestTbIdDoesNotMatchPatientTbId_AndPatientTreatmentIsAlreadyPaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.pauseCurrentTreatment("paws", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id("wrongTbId");

        assertFalse(canPauseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"No such tb id for current treatment", "Current treatment is already paused"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnTrueForCanPauseCurrentTreatmentIfPatientTreatmentCanBePaused() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertTrue(canPauseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{}, criteriaErrors.toArray());
    }
}
