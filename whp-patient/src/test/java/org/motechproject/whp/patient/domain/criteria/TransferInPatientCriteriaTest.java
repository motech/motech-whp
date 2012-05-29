package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canTransferInPatient;

public class TransferInPatientCriteriaTest {

    ArrayList<WHPErrorCode> errorCodes;

    public TransferInPatientCriteriaTest() {
        initMocks(this);
        errorCodes = new ArrayList<WHPErrorCode>();
    }

    @Test
    public void shouldReturnFalseForTransferInPatientIfCurrentTreatmentIsNotClosed() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(patient.latestTreatment().getDiseaseClass());
        patientRequest.setTreatment_category(patient.latestTreatment().getTreatmentCategory());

        assertFalse(canTransferInPatient(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_NOT_CLOSED));
    }

    @Test
    public void shouldReturnFalseForTransferInPatientIfDiseaseClassInRequestDoesNotMatchCurrentTreatmentDiseaseClass() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(DiseaseClass.E);
        patientRequest.setTreatment_category(patient.latestTreatment().getTreatmentCategory());

        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        assertFalse(canTransferInPatient(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_DETAILS_DO_NOT_MATCH));
    }

    @Test
    public void shouldReturnFalseForTransferInPatientIfTreatmentCategoryInRequestDoesNotMatchCurrentTreatmentTreatmentCategory() {
        Patient patient = new PatientBuilder().withDefaults().build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(patient.latestTreatment().getDiseaseClass());
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        patientRequest.setTreatment_category(new TreatmentCategory("Some Random Category", "11", 3, 8, 18, threeDaysAWeek));

        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        assertFalse(canTransferInPatient(patient, patientRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TREATMENT_DETAILS_DO_NOT_MATCH));
    }

    @Test
    public void shouldReturnTrueForCanTransferInPatientIfPatientTbIdMatchesRequestTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setDisease_class(patient.latestTreatment().getDiseaseClass());
        patientRequest.setTreatment_category(patient.latestTreatment().getTreatmentCategory());

        patient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        assertTrue(canTransferInPatient(patient, patientRequest, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }

}
