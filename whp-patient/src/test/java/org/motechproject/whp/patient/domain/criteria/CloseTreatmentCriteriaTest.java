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
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canCloseCurrentTreatment;

public class CloseTreatmentCriteriaTest {

     ArrayList<WHPDomainErrorCode> errorCodes;

    public CloseTreatmentCriteriaTest() {
        initMocks(this);
        errorCodes = new ArrayList<WHPDomainErrorCode>();
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentIsAlreadyClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();
        patient.closeCurrentTreatment("Cured", now());

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertFalse(canCloseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientTreatmentTbIdDoesNotMatchRequestTbId() {
        String tbId = "tbId";
        String someOtherTbId = "someOtherTbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(someOtherTbId);

        assertFalse(canCloseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TB_ID_DOES_NOT_MATCH));
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

        assertFalse(canCloseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TB_ID_DOES_NOT_MATCH));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.TREATMENT_ALREADY_CLOSED));
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientDoesNotHaveEvenASingleTreatment() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(null).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertFalse(canCloseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.NO_EXISTING_TREATMENT_FOR_CASE));
    }

    @Test
    public void shouldReturnFalseForCanCloseCurrentTreatmentIfPatientIsNull() {
        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id("caseId"); //Irrelevant as patient is passed in. Just to maintain a semblance of integrity in the test.
        treatmentUpdateRequest.setTb_id("tbId");

        assertFalse(canCloseCurrentTreatment(null, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPDomainErrorCode.CASE_ID_DOES_NOT_EXIST));
    }

    @Test
    public void shouldReturnTrueForCanCloseCurrentTreatmentIfPatientTreatmentCanBeClosed() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setTb_id(tbId);

        assertTrue(canCloseCurrentTreatment(patient, treatmentUpdateRequest, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }
}
