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
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.*;

public class OpenTreatmentCriteriaTest {


    private ArrayList<WHPDomainErrorCode> errorCodes;

    public OpenTreatmentCriteriaTest() {
        initMocks(this);
        errorCodes = new ArrayList<WHPDomainErrorCode>();
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientAlreadyHasACurrentTreatment() {
        Patient patient = new PatientBuilder().withDefaults().build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        assertFalse(canOpenNewTreatment(patient, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TREATMENT_NOT_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        Patient patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(null).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        assertFalse(canOpenNewTreatment(patient, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.NO_EXISTING_TREATMENT_FOR_CASE));
    }

    @Test
    public void shouldReturnFalseForCanOpenNewTreatmentIfPatientIsNull() {
        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id("caseId"); //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        treatmentUpdateRequest.setTb_id("tbId");

        assertFalse(canCloseCurrentTreatment(null, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.CASE_ID_DOES_NOT_EXIST));
    }

    @Test
    public void shouldReturnTrueForCanOpenNewTreatmentIfNewTreatmentCanBeOpenedForPatient() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.closeCurrentTreatment("Cured", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());

        assertTrue(canOpenNewTreatment(patient, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }
}
