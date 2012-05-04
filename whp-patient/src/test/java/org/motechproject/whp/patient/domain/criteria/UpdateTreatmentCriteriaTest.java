package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;

public class UpdateTreatmentCriteriaTest {

    @Mock
    AllPatients allPatients;

    UpdateTreatmentCriteria updateTreatmentCriteria;

    public UpdateTreatmentCriteriaTest() {
        initMocks(this);
        updateTreatmentCriteria = new UpdateTreatmentCriteria(allPatients);
    }

    @Test
    public void shouldReturnTrueForCanCloseCurrentTreatmentIfPatientTreatmentCanBeClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertTrue(updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest));
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).withTreatmentEndDate(today()).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest));
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

        assertFalse(updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest));
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientDoesNotHaveATreatment() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(null).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canCloseCurrentTreatment(treatmentUpdateRequest));
    }

    @Test
    public void shouldReturnTrueForCanOpenNewTreatmentIfNewTreatmentCanBeOpenedForPatient() {
        Patient patient = new PatientBuilder().withDefaults().withTreatmentEndDate(today()).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertTrue(updateTreatmentCriteria.canOpenNewTreatment(treatmentUpdateRequest));
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientAlreadyHasAnOpenTreatment() {
        Patient patient = new PatientBuilder().withDefaults().build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canOpenNewTreatment(treatmentUpdateRequest));
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(null).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        assertFalse(updateTreatmentCriteria.canOpenNewTreatment(treatmentUpdateRequest));
    }
}
