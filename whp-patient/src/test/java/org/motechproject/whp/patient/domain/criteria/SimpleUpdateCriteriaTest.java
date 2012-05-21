package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.errorcode.WHPDomainErrorCode;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.*;

public class SimpleUpdateCriteriaTest {

     ArrayList<WHPDomainErrorCode> errorCodes;

    public SimpleUpdateCriteriaTest() {
        initMocks(this);
         errorCodes = new ArrayList<WHPDomainErrorCode>();
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

        assertFalse(canPerformSimpleUpdate(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.NO_EXISTING_TREATMENT_FOR_CASE));
    }

    @Test
    public void shouldReturnFalseForCanPerformSimpleUpdateIfPatientTbIdDoesNotMatchUpdateRequestTbId() {
        Patient patient = new PatientBuilder().withDefaults()
                                              .withTbId("tbId")
                                              .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("wrongTbId");

        assertFalse(canPerformSimpleUpdate(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TB_ID_DOES_NOT_MATCH));
    }

    @Test
    public void shouldReturnFalseForCanPerformSimpleUpdateIfPatientIsNull() {
        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id("wrongCaseId"); //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.

        assertFalse(canPerformSimpleUpdate(null, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.CASE_ID_DOES_NOT_EXIST));
    }

    @Test
    public void shouldReturnTrueForCanPerformSimplyUpdateIfSimpleUpdateCanBePerformedForPatient() {
        Patient patient = new PatientBuilder().withDefaults()
                .withTbId("elevenDigit")
                .build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());
        patientRequest.setTb_id("elevenDigit");

        assertTrue(canPerformSimpleUpdate(patient, patientRequest, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }
}
