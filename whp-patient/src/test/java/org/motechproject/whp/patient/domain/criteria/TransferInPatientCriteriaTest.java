package org.motechproject.whp.patient.domain.criteria;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canTransferInPatient;

public class TransferInPatientCriteriaTest {

     ArrayList<WHPErrorCode> errorCodes;

    public TransferInPatientCriteriaTest() {
        initMocks(this);
         errorCodes = new ArrayList<WHPErrorCode>();
    }

    @Test
    public void shouldReturnFalseForTransferInPatientIfPatientTbIdDoesNotMatchRequestTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setOld_tb_id("wrongTbId");

        assertFalse(canTransferInPatient(patient, treatmentUpdateRequest, errorCodes));
        assertTrue(errorCodes.contains(WHPErrorCode.TB_ID_DOES_NOT_MATCH));
    }

    @Test
    public void shouldReturnTrueForCanTransferInPatientIfPatientTbIdMatchesRequestTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setOld_tb_id(tbId);

        assertTrue(canTransferInPatient(patient, treatmentUpdateRequest, errorCodes));
        assertArrayEquals(new String[]{}, errorCodes.toArray());
    }

}
