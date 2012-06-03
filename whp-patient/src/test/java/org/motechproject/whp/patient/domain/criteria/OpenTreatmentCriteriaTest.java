package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canCloseCurrentTreatment;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canOpenNewTreatment;

public class OpenTreatmentCriteriaTest {


    private ArrayList<WHPErrorCode> errorCodes;

    public OpenTreatmentCriteriaTest() {
        initMocks(this);
        errorCodes = new ArrayList<WHPErrorCode>();
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientAlreadyHasACurrentTreatment() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());

        assertFalse(canOpenNewTreatment(patient, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_NOT_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentTreatment(null).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());

        assertFalse(canOpenNewTreatment(patient, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE));
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientIsNull() {
        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id("caseId"); //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        patientRequest.setTb_id("tbId");

        assertFalse(canCloseCurrentTreatment(null, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.CASE_ID_DOES_NOT_EXIST));
    }

    @Test
    public void shouldReturnTrueForCanOpenNewTreatmentIfNewTreatmentCanBeOpenedForPatient() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setCase_id(patient.getPatientId());

        assertTrue(canOpenNewTreatment(patient, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }
}
