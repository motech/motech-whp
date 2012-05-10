package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canCloseCurrentTreatment;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canOpenNewTreatment;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canPerformSimpleUpdate;

public class UpdatePatientCriteriaTest {

    CriteriaErrors criteriaErrors;

    public UpdatePatientCriteriaTest() {
        initMocks(this);
        criteriaErrors = new CriteriaErrors();
    }

    @Test
    public void shouldReturnTrueForCanCloseCurrentTreatmentIfPatientTreatmentCanBeClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertTrue(canCloseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnTrueForCanOpenNewTreatmentIfNewTreatmentCanBeOpenedForPatient() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.closeCurrentTreatment("Cured", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        assertTrue(canOpenNewTreatment(patient, criteriaErrors));
        assertArrayEquals(new String[]{}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnTrueForCanPerformSimplyUpdateIfSimpleUpdateCanBePerformedForPatient() {
        Patient patient = new PatientBuilder().withDefaults()
                                              .withTbId("elevenDigit")
                                              .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");

        assertTrue(canPerformSimpleUpdate(patient, patientRequest, criteriaErrors));
        assertArrayEquals(new String[]{}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment("Cured", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertFalse(canCloseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors));
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

        assertFalse(canCloseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"No such tb id for current treatment"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentTbIdDoesNotMatchRequestTbIdAndTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        String someOtherTbId = "someOtherTbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment("Cured", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(someOtherTbId);

        assertFalse(canCloseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"No such tb id for current treatment", "Current treatment is already closed"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(null).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertFalse(canCloseCurrentTreatment(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Case does not have any current treatment"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientIsNull() {
        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id("caseId"); //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        treatmentUpdateRequest.setTb_id("tbId");

        assertFalse(canCloseCurrentTreatment(null, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Invalid case-id. No such patient."}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientAlreadyHasACurrentTreatment() {
        Patient patient = new PatientBuilder().withDefaults().build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        assertFalse(canOpenNewTreatment(patient, criteriaErrors));
        assertArrayEquals(new String[]{"Current treatment is not closed"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(null).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        assertFalse(canOpenNewTreatment(patient, criteriaErrors));
        assertArrayEquals(new String[]{"Case does not have any current treatment"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientIsNull() {
        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id("caseId"); //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        treatmentUpdateRequest.setTb_id("tbId");

        assertFalse(canCloseCurrentTreatment(null, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Invalid case-id. No such patient."}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanPerformSimpleUpdateIfPatientDoesNotHaveEvenASingleTreatment() {
        Patient patient = new PatientBuilder().withDefaults()
                                              .withTbId("elevenDigit")
                                              .withCurrentProvidedTreatment(null)
                                              .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");

        assertFalse(canPerformSimpleUpdate(patient, patientRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Case does not have any current treatment"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanPerformSimpleUpdateIfPatientTbIdDoesNotMatchUpdateRequestTbId() {
        Patient patient = new PatientBuilder().withDefaults()
                                              .withTbId("tbId")
                                              .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");

        assertFalse(canPerformSimpleUpdate(patient, patientRequest, criteriaErrors));
        assertArrayEquals(new String[]{"No such tb id for current treatment"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnFalseForCanPerformSimpleUpdateIfPatientIsNull() {
        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id("wrongCaseId"); //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.

        assertFalse(canPerformSimpleUpdate(null, patientRequest, criteriaErrors));
        assertArrayEquals(new String[]{"Invalid case-id. No such patient."}, criteriaErrors.toArray());
    }
}
