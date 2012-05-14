package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.*;

public class SimpleUpdateCriteriaTest {

    CriteriaErrors criteriaErrors;

    public SimpleUpdateCriteriaTest() {
        initMocks(this);
        criteriaErrors = new CriteriaErrors();
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
}
