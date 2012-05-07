package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;

public class UpdateTreatmentCriteriaTest {

    @Mock
    AllPatients allPatients;

    UpdateTreatmentCriteria updateTreatmentCriteria;

    CriteriaErrors criteriaErrors;

    public UpdateTreatmentCriteriaTest() {
        initMocks(this);
        updateTreatmentCriteria = new UpdateTreatmentCriteria(allPatients);
        criteriaErrors = new CriteriaErrors();
    }

    @Test
    public void shouldReturnTrueForCanCloseCurrentTreatmentIfPatientTreatmentCanBeClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertTrue(updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnTrueForCanOpenNewTreatmentIfNewTreatmentCanBeOpenedForPatient() {
        Patient patient = new PatientBuilder().withDefaults().withTreatmentEndDate(today()).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertTrue(updateTreatmentCriteria.canOpenNewTreatment(treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).withTreatmentEndDate(today()).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Current treatment is already closed"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentTbIdDoesNotMatchRequestTbId() {
        String tbId = "tbId";
        String someOtherTbId = "someOtherTbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(someOtherTbId);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"No such tb id for current treatment"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentTbIdDoesNotMatchRequestTbIdAndTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        String someOtherTbId = "someOtherTbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).withTreatmentEndDate(today()).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(someOtherTbId);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"No such tb id for current treatment", "Current treatment is already closed"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientDoesNotHaveATreatment() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(null).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Case does not have any current treatment"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientAlreadyHasACurrentTreatment() {
        Patient patient = new PatientBuilder().withDefaults().build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canOpenNewTreatment(treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Current treatment is not closed"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(null).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canOpenNewTreatment(treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Case does not have any current treatment"}, criteriaErrors.toArray());
    }
}
