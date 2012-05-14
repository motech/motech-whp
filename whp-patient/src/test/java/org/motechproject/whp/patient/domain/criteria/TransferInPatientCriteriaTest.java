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
import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.*;

public class TransferInPatientCriteriaTest {

    CriteriaErrors criteriaErrors;

    public TransferInPatientCriteriaTest() {
        initMocks(this);
        criteriaErrors = new CriteriaErrors();
    }

    @Test
    public void shouldReturnFalseForTransferInPatientIfPatientTbIdDoesNotMatchRequestTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setOld_tb_id("wrongTbId");

        assertFalse(canTransferInPatient(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{"No such tb id for current treatment"}, criteriaErrors.toArray());
    }

    @Test
    public void shouldReturnTrueForCanTransferInPatientIfPatientTbIdMatchesRequestTbId() {
        String tbId = "tbId";
        Patient patient = new PatientBuilder().withDefaults().withTbId(tbId).build();

        TreatmentUpdateRequest treatmentUpdateRequest = new TreatmentUpdateRequest();
        treatmentUpdateRequest.setCase_id(patient.getPatientId());
        treatmentUpdateRequest.setOld_tb_id(tbId);

        assertTrue(canTransferInPatient(patient, treatmentUpdateRequest, criteriaErrors));
        assertArrayEquals(new String[]{}, criteriaErrors.toArray());
    }

}
